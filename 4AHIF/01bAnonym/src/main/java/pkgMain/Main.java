/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgMain;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 *
 * @author Gerald
 */
public class Main {
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        Animal a1 = new Animal("dog");
        Animal a2 = new Animal("ant");
        Animal a3 = new Animal("bird");
        int i = 99;		//see compare() below
        TreeSet<Animal> ts = new TreeSet<>();
        ts.add(a1);
        ts.add(a2);
        ts.add(a3);
        System.out.println("== 1 ==" );
        printTree(ts);
        AnimalComparator ac = new AnimalComparator();
        TreeSet<Animal> ts2 = new TreeSet<>(ac);
        ts2.addAll(ts);
        System.out.println("== 2 ==" );
        printTree(ts2);
        TreeSet<Animal> ts3 = new TreeSet<>(new Comparator<Animal>() {
            @Override
            public int compare(Animal o1, Animal o2) {
            	//int y = i;   => syntax-problem, because 'final' missing!
                return o2.getId() - o1.getId();
            }
            
        });
        ts3.addAll(ts);
        System.out.println("== 3 ==" );
        printTree(ts3);
    }
    static private void printTree(Collection<Animal> coll) {
        for (Animal a: coll){
            System.out.println("  .." + a);
        }
    }
}
