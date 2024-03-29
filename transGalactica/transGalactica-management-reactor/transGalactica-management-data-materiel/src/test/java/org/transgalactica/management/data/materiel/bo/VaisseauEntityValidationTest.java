package org.transgalactica.management.data.materiel.bo;

import static org.junit.Assert.assertEquals;

import java.net.MalformedURLException;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;

import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.transgalactica.management.data.materiel.bo.VaisseauEntity;
import org.transgalactica.management.data.materiel.bo.impl.JpaVaisseauEntity;
import org.transgalactica.test.AbstractContextTest;

public class VaisseauEntityValidationTest extends AbstractContextTest {

	@Autowired
	private Validator validator;

	@Test
	public void testValidate() throws MalformedURLException {
		VaisseauEntity vaisseau = (VaisseauEntity) BeanUtils.instantiateClass(JpaVaisseauEntity.class);
		vaisseau.setImmatriculation("immatriculation");
		vaisseau.setModele("modele");

		Set<ConstraintViolation<VaisseauEntity>> constraintViolations = validator.validate(vaisseau);

		assertEquals(0, constraintViolations.size());
	}

	@Test
	public void testValidateImmatriculationAbsente() throws MalformedURLException {
		VaisseauEntity vaisseau = (VaisseauEntity) BeanUtils.instantiateClass(JpaVaisseauEntity.class);
		vaisseau.setImmatriculation(null);
		vaisseau.setModele("modele");

		Set<ConstraintViolation<VaisseauEntity>> constraintViolations = validator.validate(vaisseau);

		assertEquals(1, constraintViolations.size());
	}

	@Test
	public void testValidateImmatriculationVide() throws MalformedURLException {
		VaisseauEntity vaisseau = (VaisseauEntity) BeanUtils.instantiateClass(JpaVaisseauEntity.class);
		vaisseau.setImmatriculation(" \t");
		vaisseau.setModele("modele");

		Set<ConstraintViolation<VaisseauEntity>> constraintViolations = validator.validate(vaisseau);

		assertEquals(1, constraintViolations.size());
	}

	@Test
	public void testValidateModeleAbsente() throws MalformedURLException {
		VaisseauEntity vaisseau = (VaisseauEntity) BeanUtils.instantiateClass(JpaVaisseauEntity.class);
		vaisseau.setImmatriculation("immatriculation");
		vaisseau.setModele(null);

		Set<ConstraintViolation<VaisseauEntity>> constraintViolations = validator.validate(vaisseau);

		assertEquals(1, constraintViolations.size());
	}

	@Test
	public void testValidateModeleVide() throws MalformedURLException {
		VaisseauEntity vaisseau = (VaisseauEntity) BeanUtils.instantiateClass(JpaVaisseauEntity.class);
		vaisseau.setImmatriculation("immatriculation");
		vaisseau.setModele(" \t");

		Set<ConstraintViolation<VaisseauEntity>> constraintViolations = validator.validate(vaisseau);

		assertEquals(1, constraintViolations.size());
	}
}
