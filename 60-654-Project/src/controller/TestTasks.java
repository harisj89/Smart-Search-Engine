package controller;

import java.io.File;
import java.util.List;

import org.hibernate.HibernateException;
import org.hibernate.Session;

import model.WebFile;


public class TestTasks {

	public static void main(String[] args) { 
		String url = TestTasks.class.getClassLoader()
		.getResource("WebFiles").getPath();

File dir = new File(url);
//		for(File f : dir.listFiles()){ 
//			System.out.println(f.getName());
//		}
		
		
//		WebFile w = new WebFile();
		
		Session session = HibernateUtil.getSessionFactory().getCurrentSession();
        session.beginTransaction();
        List<WebFile> contacts = null;
        try {
             
            contacts = (List<WebFile>)session.createQuery("from WebFile").list();
            
            for(WebFile w: contacts){ 
            	System.out.println(w.getFileName());
            }
             
        } catch (HibernateException e) {
            e.printStackTrace();
            session.getTransaction().rollback();
        }
        session.getTransaction().commit();
	}
	
}
