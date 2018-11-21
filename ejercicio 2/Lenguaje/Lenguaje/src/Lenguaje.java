package src;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class Lenguaje {
	private static void createNewFile(File fichero) {
		try {
			fichero.createNewFile();
		} catch (IOException e) {
			System.out.println("error al crear el fichero" + e.getMessage());
			System.exit(1); // finalizar
		}
	}

	public static void main(String[] args) {
		/*
		 * Variables que voy a necesitar
		 */
		int numeroPalabras = 0;
		String nombreFichero = null;
		String delim = "";
		String texto = "";
		File dir;
		FileReader leer = null;
		String linea = "";
		FileWriter escribir = null;
		PrintWriter pw = null;
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// generar de forma aleatoria el número indicado por entrada
		try {
			String data;
			if(args.length!=0) {
				data = args[0];
				System.out.println("args");
			}else {
				System.out.println("read");
				data = br.readLine();
			}
			numeroPalabras = Integer.parseInt(data);
		} catch (NumberFormatException | IOException e) {
			System.out.println("Error no se ha introducido un número o error de E/S, causa :  " + e.getCause());
			System.exit(1);// salir si no es entero o error E/S
		}

		// nombre fichero que he señalado
		try {
			
			if(args.length!=0) {
				nombreFichero = args[1];
			}else {
				nombreFichero = br.readLine();
			}
		} catch (NumberFormatException | IOException e) {
			System.out.println("Error no se ha introducido un número o error de E/S, causa :  " + e.getCause());
			System.exit(1);// salir si no es entero o error E/S
		}

		/*
		 * En este paso genero las palabras aleatorias. Establezco como condición
		 * subjetiva que tenga entre 1 y 9 letras, el caracteres del 26 al 90 (A-Z)
		 * 
		 */
		for (int i = 0; i < numeroPalabras; i++) {
			int letrastotales = (int) (Math.random() * 10 + 1);
			texto += delim;
			for (int j = 0; j < letrastotales; j++) {
				texto += (char) ((int) (Math.random() * 26 + 65));
			}
			delim = "\r\n"; // salto de línea
			System.out.println(texto);
		}

		/*
		 * El siguiente paso sirve para comprobar el sistema operativo Por subjetividad
		 * le pongo la ruta para windows C://lenguajePSP sino se guarda en la misma ruta
		 * del proyecto, y para linux /lenguajePSP/
		 */
		String osName = System.getProperty("os.name");
		if (osName.toUpperCase().contains("WIN")) { // si es windows reemplazo la cadena
			dir = new File("C:\\lenguajePSP\\");
			nombreFichero = "C:\\lenguajePSP\\" + nombreFichero.replace("\\", "\\\\");
		} else {
			dir = new File("/lenguajePSP/");
			nombreFichero = "/lenguajePSP/" + nombreFichero;

		}

		/*
		 * Antes de guardar los datos compruebo si el fichero existe, sino lo creo. Si
		 * ya existe conservo sus datos.
		 */
		File fichero = new File(nombreFichero);
		if (!fichero.exists()) {// si el fichero no existe lo creo
			if (!dir.exists() && dir.mkdirs()) { // si no existe el directorio y se ha creado
				createNewFile(fichero);
			} else {
				createNewFile(fichero);
			}

		} else { // si el fichero existe, recupero sus datos
			try {
				leer = new FileReader(fichero);
				br = new BufferedReader(leer);
				System.out.println(br);
				String lineatmp = "";
				while ((lineatmp = br.readLine()) != null) {
					System.out.println("dentro readLine");
					linea += lineatmp.toString();
					linea += "\r\n";
				}
				System.out.println("lineas en fichero " + linea);
				leer.close();
			} catch (Exception e) {
				System.out.println("Error al leer del fichero" + e.getMessage());
				System.exit(1); // finalizar
			}

		}

		/*
		 * guardo el contenido en el fichero
		 */
		try {
			escribir = new FileWriter(nombreFichero);
			pw = new PrintWriter(escribir);
			pw.println(linea + texto);
		} catch (IOException e) {
			System.out.println("Error E/S");
			System.exit(1); // si hay error finalizamos
		} finally {
			try {
				// cerrar fichero
				if (escribir != null) {
					escribir.close();
				}
			} catch (IOException e) {
				System.out.println("Error al cerrar el fichero ");
				System.exit(1); // si hay error finalizamos
			}
		}

	}

}
