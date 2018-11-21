package src;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.io.RandomAccessFile;
import java.nio.channels.FileLock;

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
		int proceso = 0;
		int numeroPalabras = 0;
		String nombreFichero = null;
		String delim = "";
		String texto = "";
		File dir;
		FileReader leer = null;
		String linea = "";
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		// generar de forma aleatoria el número indicado por entrada
		if (args.length > 0) {
			proceso = Integer.parseInt(args[0]); // proceso número
			// nombre fichero que he señalado
			try {
				nombreFichero = args[1];
			} catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
				System.out.println("Error no se ha introducido un número o error de E/S, causa :  " + e.getCause());
				System.exit(1);// salir si no es entero o error E/S
			}
			try {
				// Redirección salida y error estándar
				PrintStream ps = new PrintStream(
						new BufferedOutputStream(new FileOutputStream(new File("javalog.txt"), true)), true);
				System.setOut(ps);
				System.setErr(ps);
			} catch (Exception e) {
				System.err.println("Error redirección " + e.getCause());
			}
		}else {
			System.out.println("vacío");
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
				String lineatmp = "";
				while ((lineatmp = br.readLine()) != null) {
					linea += lineatmp.toString();
					linea += "\r\n";
				}
				leer.close();
			} catch (Exception e) {
				System.out.println("Error al leer del fichero" + e.getMessage());
				System.exit(1); // finalizar
			}

		}

		//Escribo en el fichero
		File archivo = null;
		RandomAccessFile raf = null;
		FileLock bloqueo = null;
		archivo = new File(nombreFichero);
		int i = 0;
		while (i < 100) {// aumentamos situaciones concurrencia
			try {
				raf = new RandomAccessFile(archivo, "rwd"); // Abrimos el fichero
				// ***************
				// Sección crítica
					bloqueo = raf.getChannel().lock();
					// bloqueamos el canal de acceso al fichero. Obtenemos el objeto que
					// representa el bloqueo para después poder liberarlo
					System.out.println("Proceso  activo " + proceso);
					// Lectura del fichero, me da igual que esté o no vacío
					raf.writeUTF(texto); // escribimos el valor				
					i++;
					bloqueo.release(); // Liberamos el bloqueo del canal del fichero
					bloqueo = null;
				// Fin sección crítica
				// *******************
				Thread.sleep(3000); // Simulamos tiempo de creación del dato
			} catch (Exception e) {
				System.err.println("error " + e.toString());
			} finally {
				try {
					if (null != raf)
						raf.close();
				} catch (Exception e2) {
					System.err.println("Error al cerrar el fichero.");
					System.err.println(e2.toString());
					System.exit(1); // Si hay error, finalizamos
				}
			}
		}

	}

}
