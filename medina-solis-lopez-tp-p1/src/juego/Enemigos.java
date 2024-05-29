package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Enemigos {
    // VARIABLES DE INSTANCIA
	
    Bala bala; // OBJETO BALA DEL ENEMIGO
    int vidaBowser; // VIDA DEL JEFE BOWSER
    
    private Image spriteIzq; // IMAGEN DEL ENEMIGO MIRANDO A LA IZQUIERDA
    private Image spriteDer; // IMAGEN DEL ENEMIGO MIRANDO A LA DERECHA
    private Image jefeIzq; // IMAGEN DEL JEFE BOWSER MIRANDO A LA IZQUIERDA
    private Image jefeDer; // IMAGEN DEL JEFE BOWSER MIRANDO A LA DERECHA
    
    boolean direccionDerecha; // DIRECCIÓN DEL ENEMIGO (FALSE = IZQUIERDA)
    boolean estaApoyado; // INDICA SI EL ENEMIGO ESTÁ APOYADO EN EL SUELO
    boolean haDisparado; // INDICA SI EL ENEMIGO HA DISPARADO
    
    private double x, y, escalaAlto, escalaAncho, alto, ancho, velocidad, altoBowser, anchoBowser, escalaBowser; //POSICION Y DIMENSIONES
    private long ultimoDisparo, intervaloDisparo;
    
    // CONSTRUCTOR DEL ENEMIGO
    public Enemigos(double x, double y) {
        this.x = x;
        this.y = y;
        this.spriteIzq = Herramientas.cargarImagen("dinoizq.png");
        this.spriteDer = Herramientas.cargarImagen("dinoder.png");
        this.jefeIzq = Herramientas.cargarImagen("bowserIzq.png");
        this.jefeDer = Herramientas.cargarImagen("bowserDer.png");
        this.direccionDerecha = Math.random() < 0.5; // DIRECCIÓN INICIAL ALEATORIA
        
        estaApoyado = false;
        vidaBowser = 3;
        escalaAlto = 0.171;
        escalaAncho = 0.151;
        escalaBowser = 0.8;
        
        alto = spriteIzq.getHeight(null) * escalaAlto;
        ancho = spriteIzq.getWidth(null) * escalaAncho;
        altoBowser = jefeIzq.getHeight(null) * escalaBowser;
        anchoBowser = jefeIzq.getWidth(null) * escalaBowser;
        
        velocidad = 1;
        ultimoDisparo = System.currentTimeMillis();
        intervaloDisparo = 3000; // INTERVALO DE 3 SEGUNDOS ENTRE DISPAROS
    }

    // DIBUJA EL ENEMIGO EN LA PANTALLA
    public void mostrar(Entorno e) {
        if (direccionDerecha) {
            e.dibujarImagen(spriteDer, x, y, 0, escalaAlto);
        } else {
            e.dibujarImagen(spriteIzq, x, y, 0, escalaAlto);
        }
    }

    // DIBUJA AL JEFE BOWSER EN LA PANTALLA
    public void mostrarBowser(Entorno e) {
        if (direccionDerecha) {
            e.dibujarImagen(jefeDer, x, y, 0, escalaBowser);
        } else {
            e.dibujarImagen(jefeIzq, x, y, 0, escalaBowser);
        }
    }

    // MUEVE EL ENEMIGO HORIZONTALMENTE
    public void movimientoEnemigo() {
        if (estaApoyado) {
            if (direccionDerecha) {
                this.x += velocidad; // MOVER A LA DERECHA
            } else {
                this.x -= velocidad; // MOVER A LA IZQUIERDA
            }
        }
    }

    // HACE QUE EL ENEMIGO CAIGA
    public void caer() {
        if (!estaApoyado) {
            this.y += velocidad; // CAER
        }
    }

    // CAMBIA LA DIRECCIÓN DEL ENEMIGO
    public void cambiarDireccion() {
        direccionDerecha = !direccionDerecha;
    }

    // DETECTA COLISIÓN CON OTRO ENEMIGO
    public boolean colisionaConEnemigo(Enemigos otroEnemigo) {
        return this.getDerecho() > otroEnemigo.getIzquierdo() &&
               this.getIzquierdo() < otroEnemigo.getDerecho() &&
               this.getPiso() > otroEnemigo.getTecho() &&
               this.getTecho() < otroEnemigo.getPiso();
    }

    // DETECTA SI EL ENEMIGO CAE SOBRE OTRO ENEMIGO
    public boolean caidaSobreEnemigo(Enemigos otroEnemigo) {
        return (this.getPiso() >= otroEnemigo.getTecho() && this.getTecho() < otroEnemigo.getTecho() &&
                this.getDerecho() > otroEnemigo.getIzquierdo() && this.getIzquierdo() < otroEnemigo.getDerecho());
    }

    // VERIFICA SI EL ENEMIGO ESTÁ EN EL MISMO PISO QUE EL PERSONAJE
    public boolean mismoPiso(Personaje princesa) {
        return (this.getTecho() < princesa.getPiso() && this.getPiso() > princesa.getTecho());
    }

    // VERIFICA SI EL ENEMIGO ESTÁ MIRANDO EN LA MISMA DIRECCIÓN QUE EL PERSONAJE
    public boolean mismaDireccion(Personaje princesa) {
        return (this.x < princesa.getIzquierdo() && direccionDerecha == true || this.x > princesa.getDerecho() && direccionDerecha == false);
    }

    // EL ENEMIGO PUEDE DISPARAR SI CUMPLE LAS CONDICIONES
    public void puedeDisparar(Personaje princesa, Entorno e) {
        long tiempoActual = System.currentTimeMillis();
        if (estaApoyado && mismoPiso(princesa) && mismaDireccion(princesa) && tiempoActual - ultimoDisparo >= intervaloDisparo) {
            if (bala == null) {
                bala = new Bala(x, y, direccionDerecha);
                ultimoDisparo = tiempoActual; // ACTUALIZA EL TIEMPO DEL ÚLTIMO DISPARO
            }
        }
    }

    // MUEVE Y MUESTRA LA BALA DEL ENEMIGO
    public void moverYMostrarBalaEnemigo(Entorno e) {
        if (bala != null) {
            bala.mostrarBalaEnemigo(e);
            bala.moverseBalaEnemigo();
            if (bala.getX() < -0.1 * e.ancho() || bala.getX() > e.ancho() * 1.1) { //SI SALE DE LA PANTALLA 
                bala = null; //SE ELIMINA
                haDisparado = false; // SE RESETEA CUANDO LA BALA DESAPARECE
            }
        }
    }

    // MUEVE Y MUESTRA LA BALA DEL JEFE BOWSER
    public void moverYMostrarBalaBowser(Entorno e) {
        if (bala != null) {
            bala.mostrarBalaBowser(e);
            bala.moverseBalaEnemigo();
            if (bala.getX() < -0.1 * e.ancho() || bala.getX() > e.ancho() * 1.1) {
                bala = null;
                haDisparado = false; // SE RESETEA CUANDO LA BALA DESAPARECE
            }
        }
    }

    // RESTA UNA VIDA AL JEFE BOWSER
    public void restarVida() {
        this.vidaBowser -= 1;
    }

    //METODOS PARA OBTENER BORDES DEL ENEMIGO 
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

    //METODOS PARA OBTENER BORDES DEL JEFE BOWSER
    public double getTechoBowser() {
        return y - altoBowser / 2;
    }


    public double getPisoBowser() {
        return y + altoBowser / 2;
    }

    public double getDerechoBowser() {
        return x + anchoBowser / 2;
    }

    public double getIzquierdoBowser() {
        return x - anchoBowser / 2;
    }

    // OBTIENE LA COORDENADA X DEL ENEMIGO
    public double getX() {
        return x;
    }

    // ESTABLECE LA COORDENADA X DEL ENEMIGO
    public void setX(double x) {
        this.x = x;
    }

    // OBTIENE LA COORDENADA Y DEL ENEMIGO
    public double getY() {
        return y;
    }

    // ESTABLECE LA COORDENADA Y DEL ENEMIGO
    public void setY(double y) {
        this.y = y;
    }
}
