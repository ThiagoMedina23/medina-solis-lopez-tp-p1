package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Vida {
    // VARIABLES DE INSTANCIA
    int vida; // CANTIDAD DE VIDAS
    Image SpriteVida; // IMAGEN QUE REPRESENTA UNA VIDA
    double alto; 
    double ancho;
    double escala; 

    // CONSTRUCTOR DE LA CLASE VIDA
    public Vida(int vida) {
        this.vida = vida; // INICIALIZA LA CANTIDAD DE VIDAS
        SpriteVida = Herramientas.cargarImagen("Vida.png"); 
        escala = 0.2; 
        alto = SpriteVida.getHeight(null) * escala; 
        ancho = SpriteVida.getWidth(null) * escala; 
    }

    // MÉTODO PARA MOSTRAR UNA VIDA EN UNA POSICIÓN ESPECÍFICA
    public void mostrar(Entorno ent, int x, int y) {
        ent.dibujarImagen(SpriteVida, x, y, 0, escala); 
    }

    // MÉTODO PARA MOSTRAR TODAS LAS VIDAS RESTANTES
    public void mostrarTotalVidas(Entorno ent) {
        int x = 15; 
        int y = 532; 
        for (int i = 0; i < vida; i++) { // ITERA A TRAVÉS DE LA CANTIDAD DE VIDAS
            mostrar(ent, x, y); 
            x += 36; // MUEVE LA POSICIÓN EN X PARA EL SIGUIENTE SPRITE DE VIDA
        }
    }

    // MÉTODO PARA ELIMINAR UNA VIDA
    public void eliminarVida() {
        this.vida -= 1; 
    }

    // MÉTODO PARA AGREGAR UNA VIDA
    public void agregarVida() {
        this.vida += 1; 
    }
}
