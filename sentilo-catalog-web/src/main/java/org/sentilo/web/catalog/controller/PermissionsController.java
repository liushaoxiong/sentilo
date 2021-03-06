/*
 * Sentilo
 *   
 * Copyright (C) 2013 Institut Municipal d’Informàtica, Ajuntament de  Barcelona.
 *   
 * This program is licensed and may be used, modified and redistributed under the
 * terms  of the European Public License (EUPL), either version 1.1 or (at your 
 * option) any later version as soon as they are approved by the European 
 * Commission.
 *   
 * Alternatively, you may redistribute and/or modify this program under the terms
 * of the GNU Lesser General Public License as published by the Free Software 
 * Foundation; either  version 3 of the License, or (at your option) any later 
 * version. 
 *   
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR 
 * CONDITIONS OF ANY KIND, either express or implied. 
 *   
 * See the licenses for the specific language governing permissions, limitations 
 * and more details.
 *   
 * You should have received a copy of the EUPL1.1 and the LGPLv3 licenses along 
 * with this program; if not, you may find them at: 
 *   
 *   https://joinup.ec.europa.eu/software/page/eupl/licence-eupl
 *   http://www.gnu.org/licenses/ 
 *   and 
 *   https://www.gnu.org/licenses/lgpl.txt
 */
package org.sentilo.web.catalog.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

import org.sentilo.web.catalog.domain.Application;
import org.sentilo.web.catalog.domain.Permission;
import org.sentilo.web.catalog.domain.Provider;
import org.sentilo.web.catalog.dto.DataTablesDTO;
import org.sentilo.web.catalog.dto.PermissionsDTO;
import org.sentilo.web.catalog.search.SearchFilter;
import org.sentilo.web.catalog.search.builder.SearchFilterUtils;
import org.sentilo.web.catalog.service.ApplicationService;
import org.sentilo.web.catalog.service.CrudService;
import org.sentilo.web.catalog.service.PermissionService;
import org.sentilo.web.catalog.service.ProviderService;
import org.sentilo.web.catalog.utils.Constants;
import org.sentilo.web.catalog.utils.ModelUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("/permissions")
public class PermissionsController extends SearchController<Permission> {

	@Autowired
	private ApplicationService applicationService;

	@Autowired
	private PermissionService permissionService;

	@Autowired
	private ProviderService providerService;

	@Autowired
	private MessageSource messageSource;

	@ModelAttribute(Constants.MODEL_ACTIVE_MENU)
	public String getActiveMenu() {
		return Constants.MENU_APPLICATION;
	}

	@RequestMapping(value = "/application/{id}/add", method = RequestMethod.GET)
	public String showApplicationPermissions(@PathVariable String id, Model model) {

		addPermissionTypesToModel(model);

		//TODO Falta filtrar aquellos proveedores para los que no tiene permiso la app
		List<Provider> providers = providerService.findAll();
		List<Application> applications = applicationService.findAll();
		PermissionsDTO form = new PermissionsDTO(id, providers, applications);
		model.addAttribute(Constants.MODEL_PERMISSIONS, form);

		return Constants.VIEW_ADD_APPLICATION_PERMISSIONS;
	}

	@RequestMapping(value = "/application/{id}/add", method = RequestMethod.POST)
	public String addApplicationPermissions(@Valid PermissionsDTO permissions, BindingResult result, @PathVariable String id, Model model) {

		createPermissions(id, permissions.getSelectedProvidersIds(), permissions.getPermissionType());
		createPermissions(id, permissions.getSelectedApplicationsIds(), permissions.getPermissionType());

		return viewApplicationDetail(id, model, "permission.added");
	}

	@RequestMapping(value = "/application/{id}/remove", method = RequestMethod.POST)
	public String removeApplicationPermissions(@Valid PermissionsDTO permissions, BindingResult result, @PathVariable String id, Model model) {

		permissionService.delete(permissions.getSelectedPermissions());
		return viewApplicationDetail(id, model, "permission.deleted");
	}

	@RequestMapping("/application/{id}")
	public @ResponseBody
	DataTablesDTO getApplicationPermissions(HttpServletRequest request, Model model, Pageable pageable, @PathVariable String id, @RequestParam Integer sEcho,
			@RequestParam(required = false) String search) {
		return getPageList(model, request, pageable, sEcho, search);
	}

	@Override
	protected void doBeforeSearchPage(HttpServletRequest request, SearchFilter filter) {
		String id = SearchFilterUtils.getUriVariableValue(request, "/permissions/application/{id}", "id");
		if (StringUtils.hasText(id)) {
			filter.addParam("source", id);
		}
	}

	@Override
	protected List<String> toRow(Permission permission) {
		List<String> row = new ArrayList<String>();
		row.add(permission.getId());
		row.add(permission.getTarget());
		row.add(messageSource.getMessage("permission." + permission.getType().toString(), null, Locale.getDefault()));
		return row;
	}

	@Override
	protected CrudService<Permission> getService() {
		return permissionService;
	}

	@Override
	protected void initViewNames() {
	}

	private void createPermissions(String sourceId, String[] selectedIds, Permission.Type type) {
		for (String targetId : selectedIds) {
			Permission permission = new Permission(sourceId, targetId, type);
			permissionService.create(permission);
		}
	}

	private String viewApplicationDetail(String id, Model model, String messageCode) {
		ModelUtils.addConfirmationMessageTo(model, messageCode);
		ModelUtils.addOpenedTabTo(model, Constants.TAB_2);

		addApplicationToModel(id, model);
		return Constants.VIEW_APPLICATION_DETAIL;
	}

	private void addApplicationToModel(String id, Model model) {
		Application application = applicationService.findAndThrowErrorIfNotExist(new Application(id));
		model.addAttribute(Constants.MODEL_APPLICATION, application);
	}

	private void addPermissionTypesToModel(Model model) {
		model.addAttribute(Constants.MODEL_PERMISSION_TYPES, Permission.Type.values());
	}
}
