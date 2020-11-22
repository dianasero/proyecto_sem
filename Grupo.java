
public class Grupo extends Thread {
    
    private Persona [] integrantes;
    private Mostrador mostrador;
    private Caja caja;
    private boolean inicioPedido;
    private int nombre;

    public Grupo(int numero, int nombre, Caja caja, Mostrador mostrador){
        this.mostrador = mostrador;
        this.caja = caja;
        this.integrantes = new Persona[numero];
        // this.helados = helados;
        this.nombre = nombre;
        inicioPedido = false;
        crearPersonas();
    }
    public void run(){
        //Correr todos las personas del grupo
        System.out.println("Grupo run");
        mostrador.pedirGrupo(nombre);
        for(int i =0; i< integrantes.length;i++)
            integrantes[i].run();
        while (!isGrupoTerminado()){//Ya terminaron todos de pedir y de pagar 
            if(isGrupoPedido()){//Ya terminaron de pedir todos y comienzan a pagar
                caja.pagarGrupo(nombre);
                for(int i =0; i< integrantes.length;i++)
                    integrantes[i].setGrupoPedido(true);
            }
        }
        //Avisar a sus personas que ya terminaron todos
        for(int i =0; i< integrantes.length;i++)
            integrantes[i].setGrupoListo(true);
        System.out.println("El grupo "+ nombre+ " ha pagado y sale de la tienda.");
    }
    private void crearPersonas(){
        for(int i = 0; i < integrantes.length; i++)
            integrantes[i] = new Persona(i,mostrador, caja, nombre);
    }

    public boolean isGrupoTerminado(){
        boolean terminado = true;
        for(int i =0; i< integrantes.length;i++)
            terminado &= integrantes[i].isPagado();
        return terminado;
    }

    public boolean isGrupoPedido(){
        boolean terminado = true;
        for(int i =0; i< integrantes.length;i++)
            terminado &= integrantes[i].isPedido();
        return terminado;
    }

    public Persona [] personas(){
        return integrantes;
    }
}
