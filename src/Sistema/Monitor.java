package Sistema;

public class Monitor {
	private int cantLectores = 0;
    private boolean escribiendo = false;

    public synchronized void comienzoLectura() throws InterruptedException {

        while (escribiendo) {
            wait();
        }

        cantLectores++;
    }

    public synchronized void terminoLectura() {

        cantLectores--;

        if (cantLectores == 0) {
            notifyAll();
        }
    }

    public synchronized void comienzoEscritura() throws InterruptedException {

        while (escribiendo || cantLectores > 0) {
            wait();
        }

        escribiendo = true;
    }

    public synchronized void terminoEscritura() {

        escribiendo = false;
        notifyAll(); 
    }
}
