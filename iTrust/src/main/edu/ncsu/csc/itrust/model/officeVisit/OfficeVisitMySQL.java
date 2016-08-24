/**
 * 
 */
package edu.ncsu.csc.itrust.model.officeVisit;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import javax.annotation.Resource;
import javax.faces.bean.ManagedBean;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import edu.ncsu.csc.itrust.DBUtil;
import edu.ncsu.csc.itrust.exception.DBException;
import edu.ncsu.csc.itrust.exception.FormValidationException;
import edu.ncsu.csc.itrust.model.ValidationFormat;

/**
 * @author seelder
 *
 */
@ManagedBean
public class OfficeVisitMySQL implements Serializable, OfficeVisitDAO{
	@Resource(name="jdbc/itrust2")
	private OfficeVisitSQLLoader ovLoader;
	private static final long serialVersionUID = -8631210448583854595L;
	private  DataSource ds;
	private  OfficeVisitValidator validator;

	public OfficeVisitMySQL() throws DBException{
		ovLoader = new OfficeVisitSQLLoader();
		try {
			Context ctx = new InitialContext();
				this.ds = ((DataSource) (((Context) ctx.lookup("java:comp/env"))).lookup("jdbc/itrust"));
		} catch (NamingException e) {
			throw new DBException(new SQLException("Context Lookup Naming Exception: "+e.getMessage()));
		}
		validator = new OfficeVisitValidator(this.ds);

	}
	
	public OfficeVisitMySQL(DataSource ds){
		ovLoader = new OfficeVisitSQLLoader();
		this.ds = ds;
		validator = new OfficeVisitValidator(this.ds);
		
	}
	
	@Override
	public List<OfficeVisit> getVisitsForPatient(Long patientID) throws DBException{
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		if(ValidationFormat.NPMID.getRegex().matcher(Long.toString(patientID)).matches()){
		try {
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM officeVisit WHERE patientMID=?");
		
			pstring.setLong(1, patientID);
		
			results = pstring.executeQuery();
			
			final List<OfficeVisit> visitList = ovLoader.loadList(results);
			return visitList;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try{
				if(results !=null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
				
			}
		}
		}
		else{
			return null;
		}
		
		
	}
	@Override
	public boolean add(OfficeVisit ov) throws DBException{
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			validator.validate(ov);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1));
		}
		int results;
		try {
			conn = ds.getConnection();
			pstring = ovLoader.loadParameters(conn, pstring, ov, true);
			results = pstring.executeUpdate();
			retval = (results >0);
		}
		catch(SQLException e){
			throw new DBException(e);			
		}
		finally{

			DBUtil.closeConnection(conn, pstring);
		}
		return retval;
		
		
	}

	@Override
	public List<OfficeVisit> getAll() throws DBException {
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		try {
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM officeVisit");
			results = pstring.executeQuery();
			final List<OfficeVisit> visitList = ovLoader.loadList(results);
			return visitList;
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try{
				if(results !=null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {
				DBUtil.closeConnection(conn, pstring);
			}
		}
	}

	@Override
	public OfficeVisit getByID(long id) throws DBException {
		OfficeVisit ret = null;
		Connection conn = null;
		PreparedStatement pstring = null;
		ResultSet results = null;
		List<OfficeVisit> visitList = null;
		try {
			conn=ds.getConnection();
			pstring = conn.prepareStatement("SELECT * FROM officeVisit WHERE visitID=?");
		
			pstring.setLong(1, id);
		
			results = pstring.executeQuery();
			
			/*May update with loader instead*/
			visitList = ovLoader.loadList(results);
			if(visitList.size()>0){
				ret = visitList.get(0);
			}
		} catch (SQLException e) {
			throw new DBException(e);
		} finally {
			try{
				if(results !=null){
					results.close();
				}
			} catch (SQLException e) {
				throw new DBException(e);
			} finally {

				DBUtil.closeConnection(conn, pstring);
			}
		}
		return ret;
	}



	@Override
	public boolean update(OfficeVisit ov) throws DBException {
		boolean retval = false;
		Connection conn = null;
		PreparedStatement pstring = null;
		try {
			validator.validate(ov);
		} catch (FormValidationException e1) {
			throw new DBException(new SQLException(e1));
		}
		int results;
		
		try {
			conn = ds.getConnection();
			pstring = ovLoader.loadParameters(conn, pstring, ov, false);
			results = pstring.executeUpdate();
			retval = (results >0);
		}
		catch(SQLException e){
			throw new DBException(e);
		}
		finally{
			DBUtil.closeConnection(conn, pstring);
		}
		return retval;

	}



}
