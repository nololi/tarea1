package src;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.PrintStream;
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
		int numeroDePalabras = 0;
		String nombreFichero = null;
		File fichero = null;
		String texto = "";

		if (args.length > 0) {
			numeroDePalabras = Integer.parseInt(args[0]); // número de palabras
			try {
				// Redirección salida y error estándar
				PrintStream ps = new PrintStream(
						new BufferedOutputStream(new FileOutputStream(new File("javalog.txt"), true)), true);
				System.setOut(ps);
				System.setErr(ps);
			} catch (Exception e) {
				System.err.println("Error redirección " + e.getCause());
			}
		}

		/*
		 * El siguiente paso sirve para comprobar el sistema operativo y modificar los
		 * delimitadores según sea un SO Windows o linux
		 */
		String osName = System.getProperty("os.name");
		if (osName.toUpperCase().contains("WIN")) { // si es windows reemplazo la cadena
			if (args.length > 1)
				nombreFichero = args[1].replace("\\", "\\\\");
			else
				nombreFichero = "miFichero.txt";
		} else {
			if (args.length > 1) {
				nombreFichero = args[1];
			} else {
				nombreFichero = "miFichero.txt";
			}
		}

		/*
		 * Antes de guardar los datos compruebo si el fichero existe, sino lo creo.
		 */
		fichero = new File(nombreFichero);
		if (!fichero.exists()) {// si el fichero no existe lo creo
			createNewFile(fichero);
		}

		// Escribo en el fichero
		File archivo = null;
		RandomAccessFile raf = null;
		FileLock bloqueo = null;
		archivo = new File(nombreFichero);
		int w = 0;
		while (w <numeroDePalabras) {
			try {
				// Abrir fichero en modo rwd
				raf = new RandomAccessFile(archivo, "rwd");
				// ***************
				// Sección crítica
				// ****************
				
					// bloqueamos el canal de acceso al fichero. Necesito el objeto para liberarlo
					bloqueo = raf.getChannel().lock();
					/*
					 * En este paso genero las palabras aleatorias. Establezco como condición
					 * subjetiva que tenga entre 1 y 9 letras, el caracter del 26 al 90 (A-Z)
					 * 
					 */
	
					int letrastotales = (int) (Math.random() * 10 + 2); // cada palabra es número letras indeterminadas
					for (int j = 0; j < letrastotales; j++) { // palabra : 1 conjunto de letras A-Z
						texto += (char) (Math.random() * 25 + 65);
					}
	
	
					// Lectura del fichero, me da igual que esté o no vacío me pongo al final del
					// mismo
					raf.seek(raf.length());
					// escribimos el valor, pongo utf sino me pone caracteres extraños
					raf.writeUTF(texto +"\n");				
					w++;
	
					bloqueo.release(); // Libero bloqueo canal
					bloqueo = null;
					
				// Fin sección crítica
				// *******************

				// reinicio el valor texto
				texto = "";
			} catch (Exception e) {
				System.err.println("error " + e.toString());
			} finally {
				// liberar recursos
				try {
					if (null != raf) {
						raf.close();
					}
					if (null != bloqueo) {
						bloqueo.release();
					}
				} catch (Exception e2) {
					System.err.println("Error al cerrar el fichero.");
					System.err.println(e2.toString());
					System.exit(1); // Si hay error, finalizamos
				}
			}
		}

	}

}
