package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Bala {
    // VARIABLES DE INSTANCIA
    private double x, y, escala, alto, ancho, velocidad, escalaBalaEnemigo, velocidadBalaEnemigo, escalaFuego, altoFuego, anchoFuego;
    boolean dir; // DIRECCIÓN DE LA BALA (FALSE = IZQUIERDA)
    private Image spriteIzq, spriteDer, balaEnemigoIzq, balaEnemigoDer, fuegoIzq, fuegoDer;

    // CONSTRUCTOR
    public Bala(double x, double y, boolean direccion) {
        this.x = x;
        this.y = y;
        
        // CARGA DE IMÁGENES
        this.spriteIzq = Herramientas.cargarImagen("ParaguasIzq.png");
        this.spriteDer = Herramientas.cargarImagen("ParaguasDer.png");
        this.balaEnemigoIzq = Herramientas.cargarImagen("BalaEnemigoIzq.png");
        this.balaEnemigoDer = Herramientas.cargarImagen("BalaEnemigoDer.png");
        this.fuegoIzq = Herramientas.cargarImagen("fuegoIzq.png");
        this.fuegoDer = Herramientas.cargarImagen("fuegoDer.png");
        
        // INICIALIZACIÓN DE VARIABLES
        this.dir = direccion;
        this.escala = 0.25;
        this.escalaFuego = 0.4;
        this.escalaBalaEnemigo = 0.6;
        this.alto = spriteIzq.getHeight(null) * escala;
        this.ancho = spriteIzq.getWidth(null) * escala;
        this.altoFuego = fuegoIzq.getHeight(null) * escalaFuego;
        this.anchoFuego = fuegoIzq.getWidth(null) * escalaFuego;
        this.velocidad = 6;
        this.velocidadBalaEnemigo = 3;
    }

    // MOSTRAR BALA NORMAL
    public void mostrar(Entorno e) {
        if (dir) {
            e.dibujarImagen(spriteDer, x, y, 0, escala);
        } else {
            e.dibujarImagen(spriteIzq, x, y, 0, escala);
        }
    }

    // MOSTRAR BALA ENEMIGO
    public void mostrarBalaEnemigo(Entorno e) {
        if (dir) {
            e.dibujarImagen(balaEnemigoDer, x, y, 0, escalaBalaEnemigo);
        } else {
            e.dibujarImagen(balaEnemigoIzq, x, y, 0, escalaBalaEnemigo);
        }
    }

    // MOSTRAR BALA BOWSER
    public void mostrarBalaBowser(Entorno e) {
        if (dir) {
            e.dibujarImagen(fuegoDer, x, y, 0, escalaFuego);
        } else {
            e.dibujarImagen(fuegoIzq, x, y, 0, escalaFuego);
        }
    }

    // MOVIMIENTO BALA NORMAL
    public void moverse() {
        if (dir) {
            this.x += velocidad;
        } else {
            this.x -= velocidad;
        }
    }

    // MOVIMIENTO BALA ENEMIGO
    public void moverseBalaEnemigo() {
        if (dir) {
            this.x += velocidadBalaEnemigo;
        } else {
            this.x -= velocidadBalaEnemigo;
        }
    }

    // DETECCIÓN DE COLISIÓN ENTRE BALAS
    public boolean colisionDeBalas(Bala otraBala) {
        return (this.getDerecho() > otraBala.getIzquierdo() && this.getIzquierdo() < otraBala.getDerecho() &&
                this.getPiso() > otraBala.getTecho() && this.getTecho() < otraBala.getPiso());
    }

    // DETECCIÓN DE COLISIÓN ENTRE BALAS DE BOWSER
    public boolean colisionDeBalaBowser(Bala otraBala) {
        return (this.getDerechoFuego() > otraBala.getIzquierdo() && this.getIzquierdoFuego() < otraBala.getDerecho() &&
                this.getPisoFuego() > otraBala.getTecho() && this.getTechoFuego() < otraBala.getPiso());
    }

    // MÉTODOS PARA OBTENER BORDES DE LA BALA NORMAL
    public double getTecho() {
        return y - alto / 2;
    }

    public double getPiso() {
        return y + alto / 2;
    }

    public double getDerecho() {
        return x + ancho / 2;
    }

    public double getIzquierdo() {
        return x - ancho / 2;
    }

    // MÉTODOS PARA OBTENER BORDES DE LA BALA DE FUEGO
    public double getTechoFuego() {
        return y - altoFuego / 2;
    }

    public double getPisoFuego() {
        return y + altoFuego / 2;
    }

    public double getDerechoFuego() {
        return x + anchoFuego / 2;
    }

    public double getIzquierdoFuego() {
        return x - anchoFuego / 2;
    }

    // MÉTODOS GETTER Y SETTER PARA X Y Y
    public double getX() {
        return x;
    }

    public void setX(double x) {
        this.x = x;
    }

    public double getY() {
        return y;
    }

    public void setY(double y) {
        this.y = y;
    }
}
