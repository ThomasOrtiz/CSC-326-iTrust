package edu.ncsu.csc.itrust.unit.model.labProcedure;

import static org.junit.Assert.fail;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import javax.sql.DataSource;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.model.ConverterDAO;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedure;
import edu.ncsu.csc.itrust.model.labProcedure.LabProcedureMySQL;
import edu.ncsu.csc.itrust.unit.datagenerators.TestDataGenerator;

/**
 * Tests the LabProcedureMySQL class.
 * @author mwreesjo
 *
 */
public class LabProcedureMySQLTest {
	
	DataSource ds;
	@Mock DataSource mockDS;
	LabProcedureMySQL data;
	LabProcedure procedure;
	TestDataGenerator gen;

	@Before
	public void setUp() throws Exception {
		ds = ConverterDAO.getDataSource();
		data = new LabProcedureMySQL(ds);
		gen = new TestDataGenerator();
		gen.clearAllTables();
		
		procedure = new LabProcedure();
		procedure.setCommentary("commentary");
		procedure.setIsRestricted(true);
		procedure.setLabProcedureID(8L);
		procedure.setLabTechnicianID(9L);
		procedure.setOfficeVisitID(10L);
		procedure.setPriority(3);
		procedure.setResults("results!");
		procedure.setStatus(1L);
		procedure.setUpdatedDate(new Timestamp(100L));
	}
	
	@After
	public void tearDown() throws FileNotFoundException, SQLException, IOException {
		gen.clearAllTables();
	}

	@Test
	public void testGetByID() throws FileNotFoundException, SQLException, IOException {
		try {
			gen.labProcedure0();
			gen.labProcedure0();
		} catch (SQLException | IOException e1) {
			fail("Couldn't set up test data");
			e1.printStackTrace();
		}
		try {
			LabProcedure proc = data.getByID(1L);
			Assert.assertNotNull(proc);
			Assert.assertTrue(proc.getLabProcedureID() == 1L);
			Assert.assertTrue(proc.getStatus().getID() == 1L);
			Assert.assertEquals("This is a lo-pri lab procedure", proc.getCommentary());
			Assert.assertEquals("Foobar", proc.getResults());
		} catch (DBException e) {
			fail("Getting an existing lab procedure by ID shouldn't throw exception");
			e.printStackTrace();
		}
	}

	/**
	 * Tests the getAll() method. At the time of writing of this test, the lab
	 * procedures returned are in insertion order. We use this assumption to
	 * validate the output.
	 */
	@Test
	public void testGetAll() {
		try {
			gen.labProcedure0();
			gen.labProcedure1();
			gen.labProcedure2();
			gen.labProcedure3();
			gen.labProcedure4();
			gen.labProcedure5();
		} catch (SQLException | IOException e1) {
			fail("Couldn't set up test data");
			e1.printStackTrace();
		}
		try {
			// Returned in insertion order
			List<LabProcedure> procs = data.getAll();
			Assert.assertNotNull(procs);
			Assert.assertTrue(procs.size() == 6);
			Assert.assertEquals("This is a lo-pri lab procedure", procs.get(0).getCommentary());
			Assert.assertEquals("Foobar", procs.get(0).getResults());
			Assert.assertEquals("This is for lab tech 5000000002", procs.get(5).getCommentary());
		} catch (DBException e) {
			fail("Getting all lab procedures shouldn't throw exception");
			e.printStackTrace();
		}
	}
}
