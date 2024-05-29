package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Bloque {
    // VARIABLES DE INSTANCIA
    double x, y; // COORDENADAS DEL BLOQUE
    double alto, ancho; // ALTURA Y ANCHO DEL BLOQUE
    double escala; // ESCALA DEL BLOQUE
    Image sprite; // IMAGEN DEL BLOQUE
    boolean rompible; // TRUE SI EL BLOQUE ES ROMPIBLE, FALSE SI NO LO ES
    
    // CONSTRUCTOR DE LA CLASE BLOQUE
    public Bloque(double x, double y) {
        this.x = x; // INICIALIZA LA COORDENADA X DEL BLOQUE
        this.y = y; // INICIALIZA LA COORDENADA Y DEL BLOQUE
        rompible = true; // POR DEFECTO, EL BLOQUE ES ROMPIBLE
        
        // DETERMINA ALEATORIAMENTE SI EL BLOQUE ES ROMPIBLE O NO
        if(Math.random() > 0.5) {
            rompible = false;
        }
        
        // CARGA LA IMAGEN APROPIADA SEGÚN SI EL BLOQUE ES ROMPIBLE O NO
        if(rompible) {
            sprite = Herramientas.cargarImagen("bloque.jpg");
        }
        else {
            sprite = Herramientas.cargarImagen("bloqueIrrompible.jpg");
        }
        
        escala = 0.2; 
        alto = sprite.getHeight(null) * escala; // CALCULA EL ALTO ESCALADO DEL BLOQUE
        ancho = sprite.getWidth(null) * escala; // CALCULA EL ANCHO ESCALADO DEL BLOQUE
    }
    
    // MÉTODO PARA MOSTRAR EL BLOQUE EN LA PANTALLA
    public void mostrar(Entorno e) {
        e.dibujarImagen(sprite, x, y, 0, escala); 
    }
    
    //METODOS PARA OBTENER BORDES DEL BLOQUE
    public double getTecho(){
        return y - alto / 2; 
    }
    
    
    public double getPiso(){
        return y + alto / 2; 
    }
    
    
    public double getDerecho(){
        return x + ancho / 2; 
    }
    
    
    public double getIzquierdo(){
        return x - ancho / 2; 
    }
}
