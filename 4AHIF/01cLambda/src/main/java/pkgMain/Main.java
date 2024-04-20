/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package pkgMain;

import java.util.ArrayList;
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
		ArrayList<Animal> al = new ArrayList<>();
		al.add(a1);
		al.add(a2);
		al.add(a3);
		System.out.println("== 1 ==");
		TreeSet<Animal> ts1 = new TreeSet<>(new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getId() - o1.getId();
			}

		});
		ts1.addAll(al);
		printTree(ts1);
		System.out.println("== 2 ==");
		TreeSet<Animal> ts2 = new TreeSet<>(new Comparator<Animal>() {
			@Override
			public int compare(Animal o1, Animal o2) {
				return o2.getId() - o1.getId();
			}

		});
		ts2.addAll(al);
		printTreeLambda1(ts2);
		System.out.println("== 3 ==");
		printTreeLambda2(al);
	}

	static private void printTree(Collection<Animal> coll) {
		for (Animal a : coll) {
			System.out.println("  .." + a);
		}
	}
	static private void printTreeLambda1(Collection<Animal> coll) {
		coll.stream()
			.forEach((a) -> {
				System.out.println(" == " + a);
			});
	}
	static private void printTreeLambda2(Collection<Animal> coll) {
		coll.stream()
			.sorted((o1, o2) -> o2.getId() - o1.getId())
			.forEach(System.out::println);
	}
}
