/*
	marti
*/
import java.util.concurrent.Semaphore;
import java.util.Random;

public class Persona extends Thread {
    private int nombre;
    private String helado;
    private boolean pedido;
    private boolean pagado;
    private Mostrador mostrador;
    private Caja caja;
    private boolean grupoListo;
    private boolean grupoPedido;
    private int nombreGrupo;
    // Definir semaforo de caja y mostrador
    private static Semaphore mostradorPersona = new Semaphore(1);
    // Semaforo de caja
    private static Semaphore cajaPersona = new Semaphore(1);

    public Persona(int nombre, Mostrador mostrador, Caja caja, int grupo) {
        this.caja = caja;
        this.mostrador = mostrador;
        this.nombre = nombre;
        this.pedido = false;
        this.pagado = false;
        this.helado = "";
        nombreGrupo = grupo;
        grupoListo = false;
        grupoPedido = false;
    }

    public void run() {
        while (!grupoListo) {
            if (!pedido) {
                Random r = new Random();
                int num = r.nextInt(2);

                System.out.println("Persona " + nombre + " del grupo " + nombreGrupo + " eligiendo helado....");
                try {
                    Thread.sleep(num * 1000);
                    helado = elegirHelado(mostrador.getSabores(), num);
                    System.out.println("Persona " + nombre + " del grupo " + nombreGrupo + " eligió helado: " + helado);
                } catch (InterruptedException e1) {
                    e1.printStackTrace();
                }
                try {
                    mostradorPersona.acquire();
                    mostrador.pedirHelado(nombre, helado, nombreGrupo);
                    pedido = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                mostradorPersona.release();
            }
            if (pedido && grupoPedido) {
                try {
                    cajaPersona.acquire();
                    caja.pagar(nombre, nombreGrupo);
                    pagado = true;
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                cajaPersona.release();
            }
        }
    }

    public String elegirHelado(String [] sabores,int num){
       
        return sabores[num];
    }

    public boolean isPagado(){
        return pagado;
    }

    public boolean isPedido(){
        return pedido;
    }

    public void setGrupoListo(boolean grupo){
        this.grupoListo = grupo;
    }
    public void setGrupoPedido(boolean grupo){
        this.grupoPedido = grupo;
    }
}