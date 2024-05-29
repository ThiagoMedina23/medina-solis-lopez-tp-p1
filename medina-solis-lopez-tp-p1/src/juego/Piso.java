package juego;

import entorno.Entorno;

public class Piso {
    Bloque[] bloques; // ARREGLO DE BLOQUES QUE FORMAN EL PISO
    double y; // COORDENADA Y DEL PISO
    
    // CONSTRUCTOR DE LA CLASE PISO
    public Piso(double y) {
        Bloque testigo = new Bloque(0, 0);
        
        // CALCULA CUÁNTOS BLOQUES SE NECESITAN PARA CUBRIR EL ANCHO DE LA PANTALLA (800 PIXELES)
        bloques =  new Bloque[(int) (800 / testigo.ancho) + 1];
        this.y = y; // INICIALIZA LA COORDENADA Y DEL PISO
        
        // CREA CADA BLOQUE EN LA POSICIÓN CORRECTA
        for(int i = 0; i < bloques.length; i++) {
            bloques[i] = new Bloque((i + 0.5) * testigo.ancho, y);
        }
    }
    
    // MÉTODO PARA MOSTRAR TODOS LOS BLOQUES DEL PISO EN LA PANTALLA
    public void mostrar(Entorno e) {
        for(int i = 0; i < bloques.length; i++) {
            if(bloques[i] != null) { 
                bloques[i].mostrar(e); 
            }
        }
    }
    
    // MÉTODO PARA OBTENER LA COORDENADA Y DEL PISO
    public double getY() {
        return y;
    }
    
    // MÉTODO PARA ESTABLECER LA COORDENADA Y DEL PISO
    public void setY(double y) {
        this.y = y;
    }
}
