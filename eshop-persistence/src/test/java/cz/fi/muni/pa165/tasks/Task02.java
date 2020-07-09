package cz.fi.muni.pa165.tasks;

import java.util.*;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.PersistenceUnit;
import javax.validation.ConstraintViolationException;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.testng.AbstractTestNGSpringContextTests;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import cz.fi.muni.pa165.PersistenceSampleApplicationContext;
import cz.fi.muni.pa165.entity.Category;
import cz.fi.muni.pa165.entity.Product;

 
@ContextConfiguration(classes = PersistenceSampleApplicationContext.class)
public class Task02 extends AbstractTestNGSpringContextTests {
	private Map<String, Category> categoryMap;
	private Map<String, Product> productMap;

	@PersistenceUnit
	private EntityManagerFactory emf;

	
	private void assertContainsCategoryWithName(Set<Category> categories,
			String expectedCategoryName) {
		for(Category cat: categories){
			if (cat.getName().equals(expectedCategoryName))
				return;
		}
			
		Assert.fail("Couldn't find category "+ expectedCategoryName+ " in collection "+categories);
	}
	private void assertContainsProductWithName(Set<Product> products,
			String expectedProductName) {
		
		for(Product prod: products){
			if (prod.getName().equals(expectedProductName))
				return;
		}
			
		Assert.fail("Couldn't find product "+ expectedProductName+ " in collection "+products);
	}

	@BeforeClass
	public void testInit() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();

		categoryMap = new HashMap<>();
		productMap = new HashMap<>();

		List<String> categoryNames = new ArrayList<>(Arrays.asList("Electro", "Kitchen"));
		List<String> productNames = new ArrayList<>(
				Arrays.asList("Flashlight", "Kitchen robot", "Plate"));

		for (String name : categoryNames) {
			Category category = new Category();
			category.setName(name);
			categoryMap.put(name, category);
		}

		for (String name : productNames) {
			Product product = new Product();
			product.setName(name);
			productMap.put(name, product);
		}

		productMap.get("Flashlight").addCategory(categoryMap.get("Electro"));
		productMap.get("Kitchen robot").addCategory(categoryMap.get("Electro"));
		productMap.get("Kitchen robot").addCategory(categoryMap.get("Kitchen"));
		productMap.get("Plate").addCategory(categoryMap.get("Kitchen"));

		for (Map.Entry<String, Category> category : categoryMap.entrySet()) {
			em.persist(category.getValue());
		}

		for (Map.Entry<String, Product> product : productMap.entrySet()) {
			em.persist(product.getValue());
		}

		em.getTransaction().commit();
		em.close();
	}

	@Test
	public void testElectro() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category found = em.find(Category.class, categoryMap.get("Electro").getId());
		assertContainsProductWithName(found.getProducts(), "Flashlight");
		em.close();
	}

	@Test
	public void testKitchen() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Category found = em.find(Category.class, categoryMap.get("Kitchen").getId());
		assertContainsProductWithName(found.getProducts(), "Kitchen robot");
		assertContainsProductWithName(found.getProducts(), "Plate");
		em.close();
	}

	@Test
	public void testFlashlight() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product found = em.find(Product.class, productMap.get("Flashlight").getId());
		assertContainsCategoryWithName(found.getCategories(), "Electro");
		em.close();
	}

	@Test
	public void testKitchenRobot() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product found = em.find(Product.class, productMap.get("Kitchen robot").getId());
		assertContainsCategoryWithName(found.getCategories(), "Electro");
		assertContainsCategoryWithName(found.getCategories(), "Kitchen");
		em.close();
	}

	@Test
	public void testPlate() {
		EntityManager em = emf.createEntityManager();
		em.getTransaction().begin();
		Product found = em.find(Product.class, productMap.get("Plate").getId());
		assertContainsCategoryWithName(found.getCategories(), "Kitchen");
		em.close();
	}
}
