/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgMain;

/**
 *
 * @author Gerald
 */
public class Animal implements Comparable <Animal>{
	private int id;
    private String name = "Jerry";

    public Animal() {}
    
    public Animal(String name) {
    	this.id = (int)(Math.random() * 100);
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getId() {
		return id;
	}

	public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "Animal{" + id + ", " + name + '}';
    }

    @Override
    public int compareTo(Animal o) {
        return name.compareTo(o.name);
    }
    
    
    
}
