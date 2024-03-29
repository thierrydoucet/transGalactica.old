package org.transgalactica.web.employe.controller.impl;

import java.util.List;

import javax.inject.Inject;
import javax.validation.Valid;

import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.transgalactica.fwk.validation.exception.BusinessException;
import org.transgalactica.fwk.web.message.BusinessMessageUtils;
import org.transgalactica.management.business.hr.service.EmployeService;
import org.transgalactica.management.data.people.bo.EmployeSearchCriteria;
import org.transgalactica.management.data.people.bo.EmployeSummary;

@Controller
@RequestMapping(value = "/employes/search")
public class EmployeListeController {

	private static final String EMPLOYES_VIEW = "employe/liste";

	@Inject
	private BeanFactory beanFactory;

	@Inject
	private EmployeService employeService;

	protected EmployeListeController() {
	}

	@ModelAttribute("criteresRechercheEmploye")
	public EmployeSearchCriteria createCriteresRechercheHangar() {
		return beanFactory.getBean(EmployeSearchCriteria.class);
	}

	@RequestMapping(method = RequestMethod.GET)
	public String get() {
		return EMPLOYES_VIEW;
	}

	@RequestMapping(method = RequestMethod.POST)
	public String search(@Valid @ModelAttribute("criteresRechercheEmploye") EmployeSearchCriteria criteres,
			BindingResult result, Model model) {
		if (result.hasErrors()) {
			return EMPLOYES_VIEW;
		}
		try {
			List<EmployeSummary> employes = employeService.rechercherEmployes(criteres);
			model.addAttribute("employes", employes);
			if (employes.isEmpty()) {
				BusinessMessageUtils.addBusinessMessage(model, "transgalactica.employe.liste.resultat.vide");
			}
		}
		catch (BusinessException e) {
			BusinessMessageUtils.addBusinessException(model, e);
		}
		return EMPLOYES_VIEW;
	}
}
