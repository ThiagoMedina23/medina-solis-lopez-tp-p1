package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Personaje {
    // VARIABLES DE INSTANCIA
    private double x, y; // COORDENADAS DEL PERSONAJE
    private Image spriteIzq; 
    private Image spriteDer; 
    private boolean direccion; //(FALSE = IZQUIERDA)
    boolean estaApoyado; 
    boolean estaSaltando; 
    boolean estaViva; 
    Vida vidaPersonaje; // OBJETO VIDA DEL PERSONAJE
    private double escala, alto, ancho; // ESCALA Y DIMENSIONES DEL PERSONAJE
    int contadorSalto; // CONTADOR PARA LA ALTURA DEL SALTO
    private int velocidad, puntaje, enemigosEliminados; // VELOCIDAD, PUNTAJE Y ENEMIGOS ELIMINADOS
    int vida; // VIDAS DEL PERSONAJE
    Bala bala; // OBJETO BALA DEL PERSONAJE

    // CONSTRUCTOR DEL PERSONAJE
    public Personaje(double x, double y) {
        this.x = x;
        this.y = y;
        this.spriteIzq = Herramientas.cargarImagen("princesaizq.png");
        this.spriteDer = Herramientas.cargarImagen("princesader.png");
        vida = 3;
        this.puntaje = 0;
        this.enemigosEliminados = 0;
        vidaPersonaje = new Vida(vida);
        estaViva = true;
        setdireccion(false); // INICIALMENTE MIRANDO A LA IZQUIERDA
        contadorSalto = 0;
        estaApoyado = false;
        estaSaltando = false;
        escala = 0.3;
        alto = spriteIzq.getHeight(null) * escala;
        ancho = spriteIzq.getWidth(null) * escala;
        this.velocidad = 1;
    }

    // DIBUJA EL PERSONAJE EN LA PANTALLA
    public void mostrar(Entorno e) {
        if (isdireccion()) {
            e.dibujarImagen(spriteDer, x, y, 0, escala);
        } else {
            e.dibujarImagen(spriteIzq, x, y, 0, escala);
        }
    }
    
    // MUEVE EL PERSONAJE HORIZONTALMENTE
    public void moverse(boolean dirMov) {
        if (estaApoyado || estaSaltando) {
            if (dirMov) {
                this.x += this.velocidad; // MOVER A LA DERECHA
            } else {
                this.x -= this.velocidad; // MOVER A LA IZQUIERDA
            }
            this.setdireccion(dirMov); // ACTUALIZAR DIRECCIÓN
        }
    }
    
    // MUEVE EL PERSONAJE VERTICALMENTE
    public void movVertical() {
        if (!estaApoyado && !estaSaltando) {
            this.y += 2; // CAER
        }
        if (estaSaltando) {
            this.y -= 5; // SUBIR DURANTE EL SALTO
            contadorSalto++;
        }
        if (contadorSalto == 25) {
            contadorSalto = 0;
            estaSaltando = false; // TERMINAR SALTO
        }
    }

    // DETECTA COLISIÓN CON UN ENEMIGO
    public boolean colisionConEnemigo(Enemigos enemigo) {
        return (this.getDerecho() > enemigo.getIzquierdo() && this.getIzquierdo() < enemigo.getDerecho() &&
                this.getTecho() < enemigo.getPiso() && this.getPiso() > enemigo.getTecho());
    }

    // DETECTA COLISIÓN CON UNA BALA DE ENEMIGO
    public boolean detectarColisionBalaEnemigo(Bala bala) {
        return (this.getDerecho() > bala.getIzquierdo() && this.getIzquierdo() < bala.getDerecho() &&
                this.getPiso() > bala.getTecho() && this.getTecho() < bala.getPiso());
    }

    // MUESTRA LA VIDA DEL PERSONAJE EN LA PANTALLA
    public void mostrarVida(Entorno e) {
        this.vidaPersonaje.mostrarTotalVidas(e);
    }

    // AGREGA UNA VIDA AL PERSONAJE
    public void agregarVida() {
        this.vida += 1;
        vidaPersonaje.agregarVida();
    }

    // RESTA UNA VIDA AL PERSONAJE
    public void restarVida() {
        this.vida -= 1;
        vidaPersonaje.eliminarVida();
        if (this.vida == 0) {
            estaViva = false; // EL PERSONAJE MUERE SI LA VIDA ES 0
        }
    }

    //METODOS PARA OBTENER BORDES DEL PERSONAJE 
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

    // MÉTODOS GETTER Y SETTER PARA X e Y
    
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

    // OBTIENE LA DIRECCIÓN DEL PERSONAJE
    public boolean isdireccion() {
        return direccion;
    }

    // ESTABLECE LA DIRECCIÓN DEL PERSONAJE
    public void setdireccion(boolean direccion) {
        this.direccion = direccion;
    }

    // OBTIENE EL PUNTAJE DEL PERSONAJE
    public int getPuntaje() {
        return puntaje;
    }

    // ESTABLECE EL PUNTAJE DEL PERSONAJE
    public void setPuntaje(int puntaje) {
        this.puntaje = puntaje;
    }

    // OBTIENE LA CANTIDAD DE ENEMIGOS ELIMINADOS POR EL PERSONAJE
    public int getEnemigosEliminados() {
        return enemigosEliminados;
    }

    // ESTABLECE LA CANTIDAD DE ENEMIGOS ELIMINADOS POR EL PERSONAJE
    public void setEnemigosEliminados(int enemigosEliminados) {
        this.enemigosEliminados = enemigosEliminados;
    }
}
