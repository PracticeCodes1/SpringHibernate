package com.cg.example;

import java.util.Scanner;

import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;

import com.cg.example.dto.Employee;

public class App 
{
    public static void main( String[] args )
    {
    	Configuration con = new Configuration().configure("hibernate.cfg.xml").addAnnotatedClass(Employee.class);
        ServiceRegistry reg = new ServiceRegistryBuilder().applySettings(con.getProperties()).buildServiceRegistry(); //Removes deprecated methods
        SessionFactory sf = con.buildSessionFactory(reg);
  
        Employee fetchedEmp = null;
        
        /**
         * To pre enter data in the database use below commented code also 
         * change the configuration of hibernate.cfg.xml file as per your database type 
         */
        
        /*
        int empId = 0;
        Scanner sc = new Scanner(System.in);
      
        Session session = sf.openSession();
        Transaction tx = session.beginTransaction();
        
        System.out.println("Enter no of employee...");
        int n = sc.nextInt();
        
        for( int index=0; index < n; index++ )
        {
        Employee emp1 = new Employee();
        
        System.out.println("Enter emp Id: ");
        empId = sc.nextInt();
        emp1.setEmpId(empId);
        System.out.println("Enter emp Name");
        emp1.setEmpName(sc.next());
        System.out.println("Enter emp Designation");
        emp1.setEmpDesignation(sc.next());
        
        session.save(emp1);
        
        tx.commit();
        sc.close();
        }*/
        
        
        /**
         * The below commented code supports default Level 1 caching provided by hibernate in same session only i.e
         * If same data is requested 2 times only one query is fired by hibernate and data is stored in Level 1 caching m/m for
         * retrieving it again when same query is requested in the same session.
         */
        
        /*Session session1 = sf.openSession();
        session1.beginTransaction();
        
        fetchedEmp = (Employee)session1.get(Employee.class, 142);
        System.out.println(fetchedEmp);
        
        Employee fetchedEmp1 = (Employee)session1.get(Employee.class, 142);
        System.out.println(fetchedEmp1);
        
        session1.getTransaction().commit();
        session1.close();*/
        
        /**
         * The below code supports Level 2 caching provided by hibernate in different sessions i.e
         * If same data is requested in different sessions only one query is fired by hibernate and data is stored in Level 2 caching m/m for
         * retrieving it again when same query is requested in different sessions.
         * 
         * check entity for annotation for caching and hibernate.cfg.xml properties set for cache
         * <property name="hibernate.cache.use_second_level_cache">true</property>
		 * <property name="hibernate.cache.region.factory_class"> org.hibernate.cache.ehcache.EhCacheRegionFactory</property>
         */
        
        Session session1 = sf.openSession();
        session1.beginTransaction();
        fetchedEmp = (Employee)session1.get(Employee.class, 142); //142 is empId already saved in database
        System.out.println(fetchedEmp);
        session1.getTransaction().commit();
        session1.close();
        
        Session session2 = sf.openSession();
        session2.beginTransaction();
        fetchedEmp = (Employee)session2.get(Employee.class, 142);
        System.out.println(fetchedEmp);
        session2.getTransaction().commit();
        session2.close();
        
        /**
         * Same code for user defined query and not the inbuilt query in hibernate like for eg: get
         * <property name="hibernate.cache.use_query_cache">true</property> added in hibernate.cfg.xml 
         * 
         * if these property are removed then each time, a query is launched by hibernate and 
         * data is brought from database not from caching 
         */
        Session session3 = sf.openSession();
        session3.beginTransaction();
        Query q1 = session3.createQuery("from Employee where empId=456"); //456 is empId already saved in database
        q1.setCacheable(true);
        fetchedEmp = (Employee) q1.uniqueResult();
        System.out.println(fetchedEmp);
        session3.getTransaction().commit();
        session3.close();
        
        Session session4 = sf.openSession();
        session4.beginTransaction();
        Query q2 = session4.createQuery("from Employee where empId=456");
        q2.setCacheable(true);
        fetchedEmp = (Employee) q2.uniqueResult();
        System.out.println(fetchedEmp);
        session4.getTransaction().commit();
        session4.close();
        
    }
}
