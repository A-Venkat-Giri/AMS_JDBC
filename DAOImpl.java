	package com.dev.dao;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Scanner;

import com.dev.beans.Asset;
import com.dev.beans.AssetAllocation;
import com.dev.beans.Employee;
import com.dev.beans.UserMaster;
import com.dev.exceptions.AddAssetException;
import com.dev.exceptions.AddEmployeeException;
import com.dev.exceptions.GetAssetException;
import com.dev.exceptions.LoginException;
import com.dev.exceptions.RaiseAllocationException;
import com.dev.exceptions.RemoveAssetException;
import com.dev.exceptions.StatusException;
import com.dev.exceptions.UpdateAssetException;
import com.dev.exceptions.ValidationException;
import com.dev.validations.Validate;


public class DAOImpl implements DAO {

	Scanner sc=new Scanner(System.in);
	Validate v=new Validate();
	Connection conn=null;
	PreparedStatement pstmt=null;
	PreparedStatement pstmt1=null;
	ResultSet rs=null;
	Integer result=null;
	void jdbcConnection()
	{

		/**First we have to load the driver**/
		try
		{
			Driver div=new com.mysql.jdbc.Driver();
			DriverManager.registerDriver(div);
			String url="jdbc:mysql://localhost:3306/assetmanagement?user=root&password=root";
			conn=DriverManager.getConnection(url);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}
	}

	@Override
	
/**This function is used for login purpose**/
	public UserMaster login(Integer userid, String password) {
		UserMaster um=new UserMaster();
		jdbcConnection();
		try {

			String query="select Usertype from usermaster where UserId=? and UserPassword=?";
			pstmt=conn.prepareStatement(query);
			pstmt.setInt(1,userid);
			pstmt.setString(2, password);
			Boolean b=pstmt.execute();
			if(b)
			{
				rs=pstmt.getResultSet();
				String s = null;
				while(rs.next())
				{
					s=rs.getString(1);
				}
				um.setUsertype(s);
				return um;

			}
			else {
				throw new LoginException();
			}
		}
		catch (Exception e) {
			e.printStackTrace();
		}


		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return um;
	}




	@Override
	/**This function is used to add the asset**/
	public Asset addAsset(Asset asset) {
		jdbcConnection();
		try {
			String query="insert into asset values(?,?,?,?,?)";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,asset.getAssetid());
			pstmt.setString(2,asset.getAssetname());
			pstmt.setString(3, asset.getAssetdes());
			pstmt.setInt(4, asset.getQuantity());
			pstmt.setString(5,asset.getStatus());

			result=pstmt.executeUpdate();

			if(result>0)
			{
				System.out.println("Asset added successfully");

			}
			else
			{
				throw new AddAssetException();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		return asset;
	}


	@Override
	/**This function is used to remove the asset**/
	
	public Asset removeAsset(Integer aid) {
		Asset a=new Asset();
		try {
			jdbcConnection();

			String query="select * from asset where AssetId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,aid);

			Boolean result=pstmt.execute();

			if(result)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					a.setAssetid(rs.getInt(1));
					a.setAssetname(rs.getString(2));
					a.setAssetdes(rs.getString(3));
					a.setQuantity(rs.getInt(4));
					a.setStatus(rs.getString(5));

					String query1="delete from asset where AssetId=?";
					pstmt=conn.prepareStatement(query1);
					pstmt.setInt(1,aid);
					Integer count=pstmt.executeUpdate();
					if(count>0)
					{
						System.out.println("deleted successfully");
						return a;
					}
				}
			}

			else
			{
				System.out.println("error in deletion");
				throw new RemoveAssetException();
			}
		}
		catch (SQLException e) 
		{
			e.printStackTrace();
		}
		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}



		return null;
	}



	@Override
	/**this function is used to view all the asset**/
	public List<Asset> getAllAsset() {
		jdbcConnection();
		try {
			String query="select * from asset";
			pstmt=conn.prepareStatement(query);


			Boolean b=pstmt.execute();
			if(b)
			{
				rs=pstmt.getResultSet();
				/**while(rs.next())
				{
					System.out.println("Asset Id:"+rs.getInt(1));                                                                              
					System.out.println("Asset name:"+rs.getString("AssetName"));
					System.out.println("Asset Description:"+rs.getString(3));
					System.out.println("Asset Quantity:"+rs.getInt(4));
					System.out.println("Asset Status:"+rs.getString(5));

				}**/
			}
			else {
				throw new GetAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}                                                                                   


		return null;
	}

	@Override
	/**This function is used to add the employee**/
	public Employee addEmployee(Employee employee) {
		try {
			jdbcConnection();

			String query="insert into employee values(?, ?, ?, ?, ?, ?)";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,employee.getEmpno());
			pstmt.setString(2,employee.getEname());
			pstmt.setString(3, employee.getJob());
			pstmt.setInt(4, employee.getMgrno());
			pstmt.setString(5,employee.getHiredate());
			pstmt.setInt(6, employee.getDeptid());

			result=pstmt.executeUpdate();
			if(result>0)
			{
				System.out.println("employee added successfully");

			}
			else
			{
				throw new AddEmployeeException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}                                                                                   
		return employee;
	}

	@Override
	/**This function is used to raise the asset allocation**/
	public AssetAllocation raiseAllocation(AssetAllocation assetallocation) {
		jdbcConnection();
		try {

			String query="insert into asset_allocation values(?, ?, ?, ?, ?)";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,assetallocation.getAllocationid());
			pstmt.setInt(2,assetallocation.getAssetid());
			pstmt.setInt(3, assetallocation.getEmpno());
			pstmt.setString(4, assetallocation.getAllocationdate());
			pstmt.setString(5, assetallocation.getReleasedate());
			result=pstmt.executeUpdate();
			String query1="insert into allocation_status values(?, ?)";
			pstmt1=conn.prepareStatement(query1);
			pstmt1.setInt(1,assetallocation.getAllocationid());
			pstmt1.setString(2,"null");
			Integer result1=pstmt1.executeUpdate();

			if(result>0 && result1>0)
			{
				System.out.println("request raised successfully");


			}
			else
			{
				throw new RaiseAllocationException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}                                                                                   

		return assetallocation;

	}

	@Override
	/**This function is used to list out all the asset allocation**/
	public List<AssetAllocation> getAllAssetAllocation() {
		jdbcConnection();
		try {
			String query="select * from asset_allocation";
			pstmt=conn.prepareStatement(query);
			Boolean b=pstmt.execute();
			if(b)
			{
				rs=pstmt.getResultSet();
				String s = null;
				/**while(rs.next())
				{
					System.out.println("Allocation Id:"+rs.getInt(1));                                                                              
					System.out.println("Asset id:"+rs.getInt(2));
					System.out.println("Employee Number:"+rs.getInt(3));
					System.out.println("Allocation Date:"+rs.getDate(4));
					System.out.println("Release Date:"+rs.getDate(5));
				}**/
			}
			else {
				throw new GetAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}                                                                                   

		return null;		

	}

	@Override
	/**This function is used to set the status**/
	public Boolean setStatus(Integer allocationid) {
		jdbcConnection();
		try {
			System.out.println("Enter Status");
			String a=sc.next();
			Boolean b3=v.idValidation(a);
			jumpadmin:while(b3)
			{
				try {
					throw new ValidationException();
				}
				catch(ValidationException e1)
				{
					System.out.println("please enter in string format");
					System.out.println("enter again");
					a=sc.next();
					if(!v.idValidation(a))
					{
						break jumpadmin;
					}
				}
			}
			String query="update allocation_status set status=? where AllocationId=?";


			pstmt=conn.prepareStatement(query);
			pstmt.setString(1, a);
			pstmt.setInt(2,allocationid);

			result=pstmt.executeUpdate();
			if(result>0)
			{
				return true;

			}
			else
			{
				return false;
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}                                                                                   

		return null;
	}

	@Override
	/**This function is used to view the status**/
	public String viewStatus(Integer allocationid) {
		jdbcConnection();
		try {
			String query="select * from allocation_status where AllocationId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,allocationid);

			Boolean b=pstmt.execute();
			if(b)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					System.out.println("Allocation ID:"+rs.getInt(1));                                                                              

					if(rs.getString(2).equals("null"))
					{
						return "status not updated till now";
					}
					System.out.println("Status:"+rs.getString(2));
					return "";
				}
			}
			else {
				throw new StatusException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		return "enter correct allocation id";

	}

	@Override
	/**This function is used to update the asset**/
	public Asset updateAsset(Integer aid) {
		System.out.println("1. Update asset name");
		System.out.println("2. Update asset des");
		System.out.println("3. Update asset quantity");
		System.out.println("4. Update asset status");
		Integer choice=sc.nextInt();
		switch(choice)
		{
		case 4:	Asset a4=new Asset();
		try {
			jdbcConnection();

			String query="select * from asset where AssetId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,aid);

			Boolean result=pstmt.execute();

			if(result)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					a4.setAssetid(rs.getInt(1));
					a4.setAssetname(rs.getString(2));
					a4.setAssetdes(rs.getString(3));
					a4.setQuantity(rs.getInt(4));
					a4.setStatus(rs.getString(5));

					String query1="update asset set status=? where AssetId=?";
					pstmt=conn.prepareStatement(query1);
					System.out.println("enter the status that you want to update");
					String status=sc.next();
					pstmt.setString(1,status);
					pstmt.setInt(2, aid);
					Integer count=pstmt.executeUpdate();
					if(count>0)
					{
						System.out.println("Status updated successfully");
						a4.setStatus(status);
						return a4;
					}
				}
			}

			else
			{
				System.out.println("error in updation");
				throw new UpdateAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		case 1:	Asset a1=new Asset();
		try {
			jdbcConnection();

			String query="select * from asset where AssetId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,aid);

			Boolean result=pstmt.execute();

			if(result)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					a1.setAssetid(rs.getInt(1));
					a1.setAssetname(rs.getString(2));
					a1.setAssetdes(rs.getString(3));
					a1.setQuantity(rs.getInt(4));
					a1.setStatus(rs.getString(5));

					String query1="update asset set AssetName=? where AssetId=?";
					pstmt=conn.prepareStatement(query1);
					System.out.println("enter the asset name that you want to update");
					String name=sc.next();
					pstmt.setString(1,name);
					pstmt.setInt(2, aid);
					Integer count=pstmt.executeUpdate();
					if(count>0)
					{
						System.out.println("Asset name updated successfully");
						a1.setAssetname(name);
						return a1;
					}
				}
			}

			else
			{
				System.out.println("error in updation");
				throw new UpdateAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		case 2:	Asset a2=new Asset();
		try {
			jdbcConnection();

			String query="select * from asset where AssetId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,aid);

			Boolean result=pstmt.execute();

			if(result)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					a2.setAssetid(rs.getInt(1));
					a2.setAssetname(rs.getString(2));
					a2.setAssetdes(rs.getString(3));
					a2.setQuantity(rs.getInt(4));
					a2.setStatus(rs.getString(5));

					String query1="update asset set AssetDes=? where AssetId=?";
					pstmt=conn.prepareStatement(query1);
					System.out.println("enter the status that you want to update");
					String assetDes=sc.next();
					pstmt.setString(1,assetDes);
					pstmt.setInt(2, aid);
					Integer count=pstmt.executeUpdate();
					if(count>0)
					{
						System.out.println("AssetDes updated successfully");
						a2.setAssetdes(assetDes);
						return a2;
					}
				}
			}

			else
			{
				System.out.println("error in updation");
				throw new UpdateAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}

		case 3:Asset a3=new Asset();
		try {
			jdbcConnection();

			String query="select * from asset where AssetId=?";
			pstmt=conn.prepareStatement(query);

			pstmt.setInt(1,aid);

			Boolean result=pstmt.execute();

			if(result)
			{
				rs=pstmt.getResultSet();
				while(rs.next())
				{
					a3.setAssetid(rs.getInt(1));
					a3.setAssetname(rs.getString(2));
					a3.setAssetdes(rs.getString(3));
					a3.setQuantity(rs.getInt(4));
					a3.setStatus(rs.getString(5));

					String query1="update asset set Quantity=? where AssetId=?";
					pstmt=conn.prepareStatement(query1);
					System.out.println("enter the quantity that you want to update");
					Integer quantity=sc.nextInt();
					pstmt.setInt(1,quantity);
					pstmt.setInt(2, aid);
					Integer count=pstmt.executeUpdate();
					if(count>0)
					{
						System.out.println("Quantity updated successfully");
						a3.setQuantity(quantity);
						return a3;
					}
				}
			}

			else
			{
				System.out.println("error in updation");
				throw new UpdateAssetException();
			}
		}
		catch (SQLException e) {
			e.printStackTrace();
		}

		finally 
		{
			if(conn!= null)
			{
				try {
					conn.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
			if(pstmt!=null)
			{
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		}
		break;
		}

		return null;
	}

}
