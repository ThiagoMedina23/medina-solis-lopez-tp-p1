package juego;

import java.awt.Image;
import entorno.Entorno;
import entorno.Herramientas;

public class Menu {
    
    // VARIABLES DE INSTANCIA PARA CONTROLAR POSICIONES
    private int posicionMenu = 0;
    private int posicionControles = 0;
    
    // VARIABLES DE ESCALA PARA IMÁGENES
    private double escala = 0.5;
    private double escalaFondo = 0.9;
    private double escalaQuit = 1;
    private double escalaGanaste = 0.5;
    
    // VARIABLE PARA CONTROLAR EL TIEMPO ENTRE PRESIONAR TECLAS
    private long ultimaTecla = 0; 
    
    // IMÁGENES PARA EL MENÚ PRINCIPAL
    private Image start = Herramientas.cargarImagen("NewGame.png");
    private Image quitMenu = Herramientas.cargarImagen("QuitRosa.png");
    private Image controles = Herramientas.cargarImagen("Controls.png");
    
    // IMÁGENES PARA LOS BOTONES DEL MENÚ
    private Image imagenMenu = Herramientas.cargarImagen("peach.png");
    private Image imagenStart = Herramientas.cargarImagen("NewGame.png");
    private Image imagenStart2 = Herramientas.cargarImagen("NewGame2.png");
    private Image imagenQuit = Herramientas.cargarImagen("QuitRosa.png");
    private Image imagenQuit2 = Herramientas.cargarImagen("QuitRosa2.png");
    private Image imagenControles = Herramientas.cargarImagen("Controls.png");
    private Image imagenControles2 = Herramientas.cargarImagen("Controls2.png");
    
    // IMAGEN PARA LA PANTALLA DE GANASTE
    private Image ImagenGanaste = Herramientas.cargarImagen("win.jpg");
    
    // IMÁGENES PARA LA PANTALLA DE GAME OVER
    private Image imagenGameOver = Herramientas.cargarImagen("GameOverPrueba.png");
    private Image quit = Herramientas.cargarImagen("QuitGO.png");
    
    // IMÁGENES PARA LA PANTALLA DE CONTROLES
    private Image imagenFondoControles = Herramientas.cargarImagen("FondoControles.png");
    private Image back = Herramientas.cargarImagen("Back.png");
    private Image play = Herramientas.cargarImagen("Play.png");
    
    // IMÁGENES PARA LOS BOTONES DE LA PARTE DE CONTROLES
    private Image imagenBack = Herramientas.cargarImagen("Back.png");
    private Image imagenBack2 = Herramientas.cargarImagen("Back2.png");
    private Image imagenPlay = Herramientas.cargarImagen("Play.png");
    private Image imagenPlay2 = Herramientas.cargarImagen("Play2.png");

    public Menu(Entorno entorno) {
		
		
	}
    // MÉTODO PARA MOSTRAR EL MENÚ PRINCIPAL
    public void mostrarMenu(Entorno entorno) {    
        entorno.dibujarImagen(imagenMenu, 450, 280, 0, escalaFondo);        
        entorno.dibujarImagen(start, 120, 50, 0, escala);        
        entorno.dibujarImagen(quitMenu, 120, 240, 0, escala);
        entorno.dibujarImagen(controles, 120, 140, 0, escala);
    }

    
    // MÉTODO PARA MOSTRAR LA PANTALLA DE GANASTE
    public void mostrarGanaste(Entorno entorno) {
        entorno.dibujarImagen(ImagenGanaste, entorno.ancho() / 2, entorno.alto() / 2, 0, escalaGanaste);
    }
    
    // MÉTODO PARA MOSTRAR LA PANTALLA DE GAME OVER
    public void mostrarGameOver(Entorno entorno) {
        entorno.dibujarImagen(imagenGameOver, entorno.ancho() / 2, entorno.alto() / 2, 0);
        entorno.dibujarImagen(quit, 380, 420, 0, escalaQuit);
    }
    
    // MÉTODO PARA MOSTRAR LA PANTALLA DE CONTROLES
    public void mostrarControles(Entorno entorno) {
        entorno.dibujarImagen(imagenFondoControles, entorno.ancho() / 2, entorno.alto() / 2, 0);
        entorno.dibujarImagen(play, 270, 500, 0, escala);
        entorno.dibujarImagen(back, 520, 500, 0, escala);
    }
    
    // MÉTODO PARA CAMBIAR LA POSICIÓN DEL MENÚ Y EFECTO DE SELECCIÓN
    public void posicionMenu(Entorno entorno) {
        if (posicionMenu == 0) {
            start = imagenStart2;
            quitMenu = imagenQuit;
            controles = imagenControles;
        }
        if (posicionMenu == 1) {
            start = imagenStart;
            controles = imagenControles2;
            quitMenu = imagenQuit;
        }
        if (posicionMenu == 2) {
            start = imagenStart;
            controles = imagenControles;
            quitMenu = imagenQuit2;
        }
    }
    
    // MÉTODO PARA MANEJAR LAS TECLAS Y DETERMINAR LA POSICIÓN DEL MENÚ
    public void teclasMenu(Entorno entorno) {
        long TiempoTeclas = System.currentTimeMillis();
        if (TiempoTeclas - ultimaTecla > 300) { 
            if (entorno.estaPresionada(entorno.TECLA_ABAJO) && posicionMenu < 2) {
                posicionMenu = (posicionMenu + 1); 
                ultimaTecla = TiempoTeclas;
            } else if (entorno.estaPresionada(entorno.TECLA_ARRIBA) && posicionMenu > 0) {
                posicionMenu = (posicionMenu - 1); 
                ultimaTecla = TiempoTeclas;
            }
        }
    }
    
    // MÉTODO PARA CAMBIAR LA POSICIÓN DE LOS CONTROLES Y EFECTO DE SELECCIÓN
    public void posicionControles(Entorno entorno) {
        if (posicionControles == 0) {
            play = imagenPlay2;
            back = imagenBack; 
        }
        if (posicionControles == 1) {
            play = imagenPlay;
            back = imagenBack2;
        }
    }
    
    // MÉTODO PARA MANEJAR LAS TECLAS Y DETERMINAR LA POSICIÓN DE LOS CONTROLES
    public void teclasControles(Entorno entorno) {
        long tiempoActual = System.currentTimeMillis(); 
        if (tiempoActual - ultimaTecla > 300) {  
            if (entorno.estaPresionada(entorno.TECLA_DERECHA) && posicionControles < 1) {
                posicionControles = (posicionControles + 1); 
                ultimaTecla = tiempoActual; 
            } else if (entorno.estaPresionada(entorno.TECLA_IZQUIERDA) && posicionControles > 0) {
                posicionControles = (posicionControles - 1);  
                ultimaTecla = tiempoActual; 
            }
        }
    }
    
    // MÉTODO PARA OBTENER LA POSICIÓN DEL MENÚ
    public int getPosicionMenu() {
        return this.posicionMenu;
    }
    
    // MÉTODO PARA OBTENER LA POSICIÓN DEL MENÚ DE CONTROLES
    public int getPosicionControles() {
        return this.posicionControles;
    }
}
