package juego;
import entorno.Entorno;
import entorno.Herramientas;
import entorno.InterfaceJuego;

import java.awt.Color;
import java.awt.Image;
import java.util.Random;


public class Juego extends InterfaceJuego {
	// EL OBJETO ENTORNO QUE CONTROLA EL TIEMPO Y OTROS
	private Entorno entorno;
    private Personaje princesa;
    private Enemigos[] enemigos;
    private Enemigos bowser;
    private Piso[] pisos;
    private Bala bala;
    private Image fondo;
    private double escalaFondo = 4;
    private int estadoJuego = 1; // 1: Menu, 2: Juego, 3: Controles, 4: GameOver, 5: Ganaste
    private Menu menuJuego;
    private long ultimaPulsacionEnter = 0;
    
    public Juego() {
        // INICIALIZACIÓN DEL ENTORNO Y OTROS ELEMENTOS
        this.entorno = new Entorno(this, "Prueba del Entorno", 800, 600);
        this.fondo = Herramientas.cargarImagen("FondoLava.gif");
        this.princesa = new Personaje(350, 554);
        this.menuJuego = new Menu(entorno);
        inicializarPisosYEnemigos();
        
        // ¡Inicia el juego!
        this.entorno.iniciar();
    }

    /**
     * Durante el juego, el método tick() será ejecutado en cada instante y 
     * por lo tanto es el método más importante de esta clase. Aquí se debe 
     * actualizar el estado interno del juego para simular el paso del tiempo 
     * (ver el enunciado del TP para mayor detalle).
     */
    public void tick() {
        switch (this.estadoJuego) {
            case 1: // MENU PRINCIPAL
                // MOSTRAR Y MANEJAR EL MENÚ PRINCIPAL
                menuJuego.mostrarMenu(entorno);
                menuJuego.posicionMenu(entorno);
                menuJuego.teclasMenu(entorno);
                if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                    long tiempoActual = System.currentTimeMillis();
                    if (tiempoActual - ultimaPulsacionEnter > 300) {
                        ultimaPulsacionEnter = tiempoActual;
                        if (menuJuego.getPosicionMenu() == 0) { // NEW GAME
                            this.estadoJuego = 2; // INICIA EL JUEGO
                        } else if (menuJuego.getPosicionMenu() == 1) { // CONTROLES
                            this.estadoJuego = 3; 
                        } else if (menuJuego.getPosicionMenu() == 2) { // CERRAR JUEGO
                            System.exit(0); 
                        }
                    }
                }
                break;

            case 2: // JUEGO
                if (princesa.estaViva) {
                    juegoPrincipal();
                    // CUANDO EL JEFE MUERE Y EL PERSONAJE SALTA FUERA DE LA PANTALLA, GANASTE EL JUEGO
                    if(bowser == null && princesa.getY() < 0){
                        this.estadoJuego = 5;
                    }
                } else {
                    this.estadoJuego = 4; // GAME OVER
                }
                break;

            case 3: // CONTROLES
                // MOSTRAR Y MANEJAR LOS CONTROLES
                menuJuego.mostrarControles(entorno);
                menuJuego.posicionControles(entorno);
                menuJuego.teclasControles(entorno);
                if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                    if (menuJuego.getPosicionControles() == 0) { // EMPIEZA EL JUEGO
                        this.estadoJuego = 2;
                    } else if (menuJuego.getPosicionControles() == 1) { // VOLVER AL MENÚ PRINCIPAL
                        this.estadoJuego = 1;
                    }
                }
                break;

            case 4: // PANTALLA DE GAME OVER
                // MOSTRAR LA PANTALLA DE GAME OVER
                menuJuego.mostrarGameOver(entorno);
                if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                    System.exit(0); // CIERRA EL JUEGO
                }
                break;
            
            case 5: // PANTALLA DE GANASTE
                // MOSTRAR LA PANTALLA DE GANASTE
                menuJuego.mostrarGanaste(entorno);
                if (entorno.sePresiono(entorno.TECLA_ENTER)) {
                    System.exit(0); // CIERRA EL JUEGO
                }
                break;
        }   
    }

	
	private void juegoPrincipal() {
		//MOSTRAMOS FONDO, PRINCESA, VIDAS, PUNTOS 
		mostrarFondo(entorno);
		princesa.mostrar(entorno);
		princesa.mostrarVida(entorno);
		mostrarEnemigosEliminados(entorno, princesa);
	    mostrarPuntos(entorno, princesa);
		
		
		//MOVIMIENTO DE LA PRINCESA
		if(entorno.estaPresionada(entorno.TECLA_DERECHA)) {
			princesa.moverse(true);
		}
		if(entorno.estaPresionada(entorno.TECLA_IZQUIERDA)) {
			princesa.moverse(false);
		}
		if(bala == null && entorno.estaPresionada('x')) {
			bala = new Bala(princesa.getX(), princesa.getY(), princesa.isdireccion());
		}
		if(entorno.estaPresionada(entorno.TECLA_ESPACIO) && princesa.estaApoyado) {
			princesa.estaSaltando = true;
		}
		//LIMITE PARA QUE EL PERSONAJE NO PUEDA SALIR DE EL ANCHO DE LA PANTALLA
		if (princesa.getX() < 15) {
	        princesa.setX(15);
	    } else if (princesa.getX() >785){
	        princesa.setX(785);
	    }
		//SALTO
		princesa.movVertical();	
		
		//DISPARO DEL PERSONAJE
		if(bala != null) {
			bala.mostrar(entorno);
			bala.moverse();
		} 
		
		//MUESTRA LOS PISOS
		for(int i = 0; i < pisos.length; i++) {
			pisos[i].mostrar(entorno);
		}
		//ELIMINAR BALA CUANDO SALGA DEL ANCHO DE LA PANTALLA
		if( bala != null && (bala.getX() < -0.1 * entorno.ancho() 
		|| bala.getX() > entorno.ancho() * 1.1)) {				
			bala = null;
			
		}
		//COLISION DE LA PRINCESA CON LOS BLOQUES
		if(detectarApoyo(princesa, pisos)) { 
			princesa.estaApoyado = true;
		}
		else {
			princesa.estaApoyado = false;
		}
		
		if(detectarColision (princesa, pisos)) {
			princesa.estaSaltando = false;
			princesa.contadorSalto = 0;
		}
		//MOSTRAR AL JEFE, MOVIMIENTOS Y COLISIONES 
		if(bowser != null) {
			bowser.mostrarBowser(entorno);
			bowser.movimientoEnemigo();
			//APOYO DE BOWSER CON LOS BLOQUES 
			bowser.estaApoyado=detectarApoyoBowser(bowser, pisos);
			if(!bowser.estaApoyado) {
				bowser.caer();
			}
			//DISPARO BOWSER
			bowser.puedeDisparar(princesa, entorno);
			bowser.moverYMostrarBalaBowser(entorno);
			
			//COLISION DE LA PRINCESA CON LOS DISPAROS DE BOWSER
			if(bowser.bala != null && princesa.detectarColisionBalaEnemigo(bowser.bala)) {
				princesa.restarVida();
				bowser.bala=null;
			}
			//COLISION DE LA PRINCESA CON BOWSER
			if(princesa.colisionConEnemigo(bowser)) {
				princesa.restarVida();
			}
			//CAMBIO DE DIRECCION
			if(bowser.getDerechoBowser()>=entorno.ancho() || bowser.getIzquierdoBowser()<=0) {
				bowser.cambiarDireccion();
			}
			//COLISION DEL BOWSER CON EL DISPARO DE LA PRINCESA
            if (bala != null && detectarColision(bowser, bala)) {
            	bowser.restarVida();
                bala = null;
                if(bowser.vidaBowser==0) {
                	bowser=null;
                	princesa.setPuntaje(princesa.getPuntaje() + 10);
                	princesa.setEnemigosEliminados(princesa.getEnemigosEliminados() + 1);
            	}
            }
          //COLISION ENTRE LOS DISPAROS
            if (bala != null && bowser.bala != null && bala.colisionDeBalaBowser(bowser.bala)) {
                
                bala = null;
                bowser.bala = null;
            }
		}
		//PARTE DE LOS ENEMIGOS
		//ENEMIGOS EN PANTALLA Y MOVIMIENTO DE LOS ENEMIGOS
		for (int enem = 0; enem < enemigos.length; enem++) {
	        Enemigos enemigo = enemigos[enem];
	        if (enemigo != null) {
	            enemigo.mostrar(entorno);
	            enemigo.movimientoEnemigo();
	            //COLISION DEL ENEMIGO CON EL DISPARO DE LA PRINCESA
	            if (bala != null && detectarColision(enemigo, bala)) {
	                
	                enemigos[enem] = null;
	                bala = null;
	                princesa.setPuntaje(princesa.getPuntaje() + 2);
	                princesa.setEnemigosEliminados(princesa.getEnemigosEliminados() + 1);
	            }
	            
	            //COLISION ENTRE LOS DISPAROS
	            if (bala != null && enemigo.bala != null && bala.colisionDeBalas(enemigo.bala)) {
	                
	                bala = null;
	                enemigo.bala = null;
	            }
	            
	            //APOYO DE LOS ENEMIGOS EN LOS BLOQUES
	            enemigo.estaApoyado = detectarApoyo(enemigo, pisos);
	            if (!enemigo.estaApoyado) {
	                enemigo.caer();
	                
	            }
	            //DISPARO DEL ENEMIGO
	            enemigo.puedeDisparar(princesa, entorno);
	            enemigo.moverYMostrarBalaEnemigo(entorno);
	            
	            //COLISION DE LA PRINCESA CON LOS DISPAROS Y LOS ENEMIGOS
	            if (enemigo.bala != null && princesa.detectarColisionBalaEnemigo(enemigo.bala)) {
	                princesa.restarVida();	
	                enemigo.bala = null;
	            }
	            if (princesa.colisionConEnemigo(enemigo)) {
	                princesa.restarVida();
	            }
	            //CAMBIO DE DIRECCION DE LOS ENEMIGOS CON LOS BORDES
	            if (enemigo.getDerecho() >= entorno.ancho() || enemigo.getIzquierdo() <= 0) {
	                enemigo.cambiarDireccion();
	            }
	            //
	            for (int otroEnemigo = 0; otroEnemigo < enemigos.length; otroEnemigo++) { 
	            	if (enem != otroEnemigo && enemigos[otroEnemigo]!=null) { 
	            		if(enemigo.colisionaConEnemigo(enemigos[otroEnemigo])) {
	            			if(enemigo.estaApoyado && enemigos[otroEnemigo].estaApoyado) {
	            				enemigo.cambiarDireccion();		
	            		}
	                }
	            	//SI UN ENEMIGO VA A CAER ENCIMA DE OTRO, PARA QUE NO ESTEN CHOCANDO INDEFINIDAMENTE
	            	//EL ENEMIGO DE ABAJO PARA A SER NULL "COMO SI HUBIERA SIDO APLASTADO""	
	            	if (!enemigo.estaApoyado && enemigos[otroEnemigo].estaApoyado && enemigo.caidaSobreEnemigo(enemigos[otroEnemigo])) {
	            		enemigos[otroEnemigo] = null;
	            		}
	                }
	            }
	        }
		}
	}  
	@SuppressWarnings("unused")
	public static void main(String[] args) {
		Juego juego = new Juego();
	}
	//GENERAR PISOS Y ENEMIGOS 
	private void inicializarPisosYEnemigos() {
        pisos = new Piso[5];
        enemigos = new Enemigos[pisos.length * 2];
        bowser = new Enemigos(100,50);
        Random random = new Random();
        
        for (int i = 0; i < pisos.length; i++) {
        	
            pisos[i] = new Piso(120 + i * (entorno.alto() / pisos.length));
            if(i>0) {
            for (int j = i + 1; j < pisos.length; j++) {
            	//POSICIONES RANDOM
            	int pos1= random.nextInt(450, 700); 
            		
            	int pos2=random.nextInt(50, 400);
                
            	double y = pisos[i].getY() - 45; 
                
                enemigos[j * 2] = new Enemigos(pos1, y);
                enemigos[j * 2 + 1] = new Enemigos(pos2 , y); 
            	}
            }
        }
    }
	//COLISIONES DE LA PRINCESA
	// DETECTA SI LA PRINCESA ESTÁ EN APOYO CON UN BLOQUE
	public boolean detectarApoyo(Personaje prin, Bloque bl) {
		return Math.abs((prin.getPiso() - bl.getTecho())) < 2 && 
				(prin.getIzquierdo() < (bl.getDerecho())) &&
				(prin.getDerecho() > (bl.getIzquierdo()));		
	}

	// DETECTA SI LA PRINCESA ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN PISO
	public boolean detectarApoyo(Personaje prin, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			if(pi.bloques[i] != null && detectarApoyo(prin, pi.bloques[i])) {
				return true;
			}
		}
		
		return false;
	}
	// DETECTA SI LA PRINCESA ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN ARRAY DE PISOS
	public boolean detectarApoyo(Personaje prin, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarApoyo(prin, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	// DETECTA COLISIONES DE LA PRINCESA CON UN BLOQUE
	public boolean detectarColision(Personaje prin, Bloque bl) {
		return Math.abs((prin.getTecho() - bl.getPiso())) < 5 && 
				(prin.getIzquierdo() < (bl.getDerecho())) &&
				(prin.getDerecho() > (bl.getIzquierdo()));	
	}
	// DETECTA COLISIONES DE LA PRINCESA CON UN BLOQUE EN UN PISO
	public boolean detectarColision(Personaje prin, Piso pi) {
		for(int i = 0; i < pi.bloques.length; i++) {
			// SI EL BLOQUE ES ROMPIBLE, LO DESTRUYE Y, A VECES, AGREGA VIDA ALEATORIA A LA PRINCESA
			if(pi.bloques[i] != null && detectarColision(prin, pi.bloques[i])) {
				if(pi.bloques[i].rompible) {
					pi.bloques[i] = null;
					if(princesa.vida<5 && Math.random()<0.2) { //+VIDA ALEATORIA
						princesa.agregarVida();
						princesa.mostrarVida(entorno);
					}
				}
				return true;
			}
		}
		
		return false;
	}
	// DETECTA COLISIONES DE LA PRINCESA CON LOS BLOQUES EN UN ARRAY DE PISOS
	public boolean detectarColision(Personaje prin, Piso[] pisos) {
		for(int i = 0; i < pisos.length; i++) {
			if(detectarColision(prin, pisos[i])) {
				return true;
			}
		}
		
		return false;
	}
	// DETECTA COLISIONES DE LAS BALAS DE LOS ENEMIGOS CON LA PRINCESA
	public boolean detectarColisionBalaEnemigo(Personaje prin,Bala bala) {
	    return (bala.getX() < prin.getDerecho() && bala.getX() > prin.getIzquierdo()) &&
	           (bala.getY()< prin.getPiso() && bala.getY() > prin.getTecho());
	}
	//COLISIONES DE BOWSER
	// DETECTA SI BOWSER ESTÁ EN APOYO CON UN BLOQUE
	public boolean detectarApoyoBowser(Enemigos bowser, Bloque bl) {
		return Math.abs((bowser.getPisoBowser() - bl.getTecho())) < 20 &&
                (bowser.getIzquierdoBowser() < (bl.getDerecho())) &&
                (bowser.getDerechoBowser() > (bl.getIzquierdo()));
	}
	// DETECTA SI BOWSER ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN PISO
	public boolean detectarApoyoBowser(Enemigos bowser, Piso pi) {
	    for (int i = 0; i < pi.bloques.length; i++) {
	        if (pi.bloques[i] != null && detectarApoyoBowser(bowser, pi.bloques[i])) {
	            return true;
	        }
	    }
	    return false;
	}
	// DETECTA SI BOWSER ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN ARRAY DE PISOS
	public boolean detectarApoyoBowser(Enemigos bowser, Piso[] pisos) {
	    for (int i = 0; i < pisos.length; i++) {
	        if (detectarApoyoBowser(bowser, pisos[i])) {
	            return true;
	        }
	    }
	    return false;
	}
	// DETECTA COLISIONES DE BOWSER CON UNA BALA
	public boolean detectarColisionBowser(Enemigos bowser, Bala balin) {
	    return (balin.getX() < bowser.getDerechoBowser() && balin.getX() > bowser.getIzquierdoBowser()) &&
	           (balin.getY() < bowser.getPisoBowser() && balin.getY() > bowser.getTechoBowser());
	}
	
	//COLISIONES DE LOS ENEMIGOS
	// DETECTA SI UN ENEMIGO ESTÁ EN APOYO CON UN BLOQUE
	public boolean detectarApoyo(Enemigos enem, Bloque bl) {
        return Math.abs((enem.getPiso() - bl.getTecho())) < 2 &&
                (enem.getIzquierdo() < (bl.getDerecho())) &&
                (enem.getDerecho() > (bl.getIzquierdo()));
    }
	// DETECTA SI UN ENEMIGO ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN PISO
    public boolean detectarApoyo(Enemigos enem, Piso pi) {
        for (int i = 0; i < pi.bloques.length; i++) {
            if (pi.bloques[i] != null && detectarApoyo(enem, pi.bloques[i])) {
                return true;
            }
        }

        return false;
    }
    // DETECTA SI UN ENEMIGO ESTÁ EN APOYO CON ALGÚN BLOQUE EN UN ARRAY DE PISOS
    public boolean detectarApoyo(Enemigos enem, Piso[] pisos) {
        for (int i = 0; i < pisos.length; i++) {
            if (detectarApoyo(enem, pisos[i])) {
                return true;
            }
        }
        return false;
    }
    // DETECTA COLISIONES DE UN ENEMIGO CON UNA BALA
	public boolean detectarColision(Enemigos enem, Bala balin) {
	    return (balin.getX() < enem.getDerecho() && balin.getX() > enem.getIzquierdo()) &&
	           (balin.getY() < enem.getPiso() && balin.getY() > enem.getTecho());
	}

	//FONDO DEL JUEGO
	public void mostrarFondo(Entorno ent) {
		ent.dibujarImagen(fondo, 0, 0, 0,escalaFondo);
	}
	
	//PUNTAJE Y ENEMIGOS ELIMINADOS
	public void mostrarEnemigosEliminados(Entorno ent, Personaje prin) {
        int x = 0;
        int y = 580;
        ent.cambiarFont("Arial", 18, Color.white); 
        ent.escribirTexto("Eliminados = " + prin.getEnemigosEliminados(), x, y);
    }
    public void mostrarPuntos(Entorno ent, Personaje prin) {
        int x = 0;
        int y = 560;
        ent.cambiarFont("Arial", 18, Color.white); 
        ent.escribirTexto("Puntos = " + prin.getPuntaje(), x, y);
    }
    //ESTADO DE JUEGO
	public void SetEstadoJuego(int n) {
		this.estadoJuego=n;
	}
	
}