package org.transgalactica.data.rest.test.dao;

import static org.hamcrest.CoreMatchers.instanceOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Date;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Named;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.BeanUtils;
import org.springframework.http.client.ClientHttpRequestFactory;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.annotation.DirtiesContext.ClassMode;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;
import org.transgalactica.data.rest.bo.EmployeSearchCriteria;
import org.transgalactica.data.rest.bo.EmployeSummaryTo;
import org.transgalactica.data.rest.bo.EmployeTo;
import org.transgalactica.data.rest.bo.MecanicienTo;
import org.transgalactica.data.rest.bo.PiloteTo;
import org.transgalactica.data.rest.bo.impl.BasicEmployeSearchCriteria;
import org.transgalactica.data.rest.bo.impl.BasicMecanicienTo;
import org.transgalactica.data.rest.bo.impl.BasicPiloteTo;
import org.transgalactica.data.rest.dao.EmployeDao;
import org.transgalactica.test.AbstractContextTest;

@DirtiesContext(classMode = ClassMode.AFTER_CLASS)
public class EmployeDaoTest extends AbstractContextTest {

	@Inject
	@Named("mockClientHttpRequestFactory")
	private ClientHttpRequestFactory mock;

	@Inject
	private EmployeDao dao;

	@Before
	public void mockDao() {
		RestTemplate template = (RestTemplate) ReflectionTestUtils.getField(dao, "restTemplate");
		template.setRequestFactory(mock);
	}

	@Test
	public void testSearchByCriteria() {
		EmployeSearchCriteria criteres = BeanUtils.instantiateClass(BasicEmployeSearchCriteria.class);
		criteres.setNomEmploye("%");

		List<EmployeSummaryTo> employes = dao.searchByCriteria(criteres);

		assertEquals(7, employes.size());
		assertEquals(new Long(2), employes.get(0).getMatricule());
		assertEquals("Chewbacca", employes.get(0).getNom());
		assertEquals(234655200000L, employes.get(0).getDateEmbauche().getTime());
		assertEquals("MECANICIEN", employes.get(0).getTypeEmploye());
	}

	@Test
	public void testGetByMatricule_mecanicien() {
		EmployeTo employe = dao.getByMatricule(2L);

		assertNotNull(employe);
		assertThat(employe, instanceOf(MecanicienTo.class));
		assertEquals(new Long(2), employe.getMatricule());
		assertEquals(234655200000L, employe.getDateEmbauche().getTime());
		assertEquals("Chewbacca", employe.getNom());
		assertEquals("MECANICIEN", employe.getTypeEmploye());

		assertNotNull(employe.getVaisseaux());
		assertEquals(1, employe.getVaisseaux().size());

		assertEquals("Faucon Millenium", employe.getVaisseaux().get(0).getImmatriculation());
		assertEquals(100000, employe.getVaisseaux().get(0).getCapaciteDeFret());
		assertEquals("cargo YT-1300", employe.getVaisseaux().get(0).getModele());
		assertEquals(6, employe.getVaisseaux().get(0).getNombreDePassagers());
		assertEquals(100000, employe.getVaisseaux().get(0).getAutonomie());
		assertEquals(105, employe.getVaisseaux().get(0).getVitesse());

		assertNotNull(((MecanicienTo) employe).getSpecialites());
		assertEquals(1, ((MecanicienTo) employe).getSpecialites().size());

		assertEquals("Armement", ((MecanicienTo) employe).getSpecialites().get(0));
	}

	@Test
	public void testGetByMatricule_pilote() {
		EmployeTo employe = dao.getByMatricule(1L);

		assertNotNull(employe);
		assertThat(employe, instanceOf(PiloteTo.class));
		assertEquals(new Long(1), employe.getMatricule());
		assertEquals(234655200000L, employe.getDateEmbauche().getTime());
		assertEquals("Han Solo", employe.getNom());
		assertEquals("PILOTE", employe.getTypeEmploye());
		assertEquals(542, ((PiloteTo) employe).getNombreHeuresVol());

		assertNotNull(employe.getVaisseaux());
		assertEquals(1, employe.getVaisseaux().size());

		assertEquals("Faucon Millenium", employe.getVaisseaux().get(0).getImmatriculation());
		assertEquals(100000, employe.getVaisseaux().get(0).getCapaciteDeFret());
		assertEquals("cargo YT-1300", employe.getVaisseaux().get(0).getModele());
		assertEquals(6, employe.getVaisseaux().get(0).getNombreDePassagers());
		assertEquals(100000, employe.getVaisseaux().get(0).getAutonomie());
		assertEquals(105, employe.getVaisseaux().get(0).getVitesse());

	}

	@Test
	public void testPersist_newMecanicien() {
		EmployeTo employe = BeanUtils.instantiateClass(BasicMecanicienTo.class);
		employe.setNom("Luke");
		employe.setDateEmbauche(new Date());

		dao.persist(employe);
	}

	@Test
	public void testPersist_newPilote() {
		EmployeTo employe = BeanUtils.instantiateClass(BasicPiloteTo.class);
		employe.setNom("Luke");
		employe.setDateEmbauche(new Date());

		dao.persist(employe);
	}

	@Test
	public void testPersist_updateMecanicien() {
		EmployeTo employe = dao.getByMatricule(2L);
		employe.setNom("Chewee");

		dao.persist(employe);
	}

	@Test
	public void testPersist_updatePilote() {
		EmployeTo employe = dao.getByMatricule(1L);
		employe.setNom("Chewee");

		dao.persist(employe);
	}

	@Test
	public void testRemove() {
		dao.remove(2L);
	}

	@Test
	public void testAddVaisseau() {
		dao.addVaisseau(2L, "Serenity");
	}

	@Test
	public void removeAddVaisseau() {
		dao.removeVaisseau(2L, "Serenity");
	}

	@Test
	public void testAddSpecialite() {
		dao.addSpecialite(1L, "Blindage");
	}

	@Test
	public void testRemoveSpecialite() {
		dao.removeSpecialite(1L, "Blindage");
	}
}
