/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgMain;

import java.util.Comparator;

/**
 *
 * @author gerald
 */
public class AnimalComparator implements Comparator<Animal> {

    @Override
    public int compare(Animal o1, Animal o2) {
        return o2.getName().compareTo(o1.getName());
    }
    
}
