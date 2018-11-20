package primeraParte;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Main {
	public static void main(String[] args) {
		List<Integer> numeros = new ArrayList<>(); // para guardar y ordenar el listado
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		while (true) {
			try {
				String data = br.readLine();
				numeros.add(Integer.parseInt(data));
			} catch (NumberFormatException | IOException e) { // salir si no es entero o error E/S
				break;
			}
		}

		// ordeno la lista y muestro
		Collections.sort(numeros);
		String delim = "";
		for (Integer integer : numeros) {
			System.out.print(delim + integer);
			delim = ",";
		}
		System.out.println(".");

	}

}
