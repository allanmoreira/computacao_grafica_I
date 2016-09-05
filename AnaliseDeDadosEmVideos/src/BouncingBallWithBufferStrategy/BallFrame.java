/******************************************************************************
*
* COPYRIGHT Vin�cius G. Mendon�a ALL RIGHTS RESERVED.
*
* This software cannot be copied, stored, distributed without  
* Vin�cius G.Mendon�a prior authorization.
*
* This file was made available on http://www.pontov.com.br and it is free 
* to be restributed or used under Creative Commons license 2.5 br: 
* http://creativecommons.org/licenses/by-sa/2.5/br/
*
*******************************************************************************
* Este software nao pode ser copiado, armazenado, distribuido sem autoriza��o 
* a priori de Vin�cius G. Mendon�a
*
* Este arquivo foi disponibilizado no site http://www.pontov.com.br e est� 
* livre para distribui��o seguindo a licen�a Creative Commons 2.5 br: 
* http://creativecommons.org/licenses/by-sa/2.5/br/
*
******************************************************************************/
package BouncingBallWithBufferStrategy;

import java.awt.Color;
import java.awt.EventQueue;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JFrame;

public class BallFrame extends JFrame implements LoopSteps {
	private MainLoop loop = new MainLoop(this, 60);
	
	private long previous = System.currentTimeMillis();
	private Ball ball;	
	
	public BallFrame() {		
		super("Bouncing ball");
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		setIgnoreRepaint(true);
		setSize(400, 400);		
		addWindowListener(new WindowAdapter() {
			@Override
			public void windowClosing(WindowEvent e) {
				//Se apertar o x, paramos o loop.
				loop.stop();
			}
		});
	}
	
	public void startMainLoop()
	{
		//Iniciamos o main loop
		new Thread(loop, "Main loop").start();
	}

	@Override
	public void setup() {
		//Criamos a estrat�gia de double buffering
		createBufferStrategy(2);
		//Subtrai a decora��o da janela da largura e altura m�ximas
		//percorridas pela bola.
		ball = new Ball(getWidth() - getInsets().left - getInsets().right, 
				getHeight() - getInsets().top - getInsets().bottom);
	}
	
	@Override
	public void processLogics() {
		//Calcula o tempo entre dois updates
		long time = System.currentTimeMillis() - previous;
		
		//Chama o update dos sprites, no caso, s� a bola
		ball.update(time);

		//Grava o tempo na sa�da do método
		previous = System.currentTimeMillis();
	}

	@Override
	public void renderGraphics() {		
		Graphics g = getBufferStrategy().getDrawGraphics();
		
		//Criamos um contexto gr�fico que n�o leva em conta as bordas
		Graphics g2 = g.create(getInsets().right, 
				   getInsets().top, 
				   getWidth() - getInsets().left, 
				   getHeight() - getInsets().bottom);
		//Limpamos a tela
//		g2.setColor(Color.BLACK);
		g2.fillRect(0, 0, getWidth(), getHeight());
		
	
		ball.draw((Graphics2D) g2); //Desenhamos a bola
		
		//Liberamos os contextos criados.
		g.dispose(); 
		g2.dispose();
	}
	
	@Override
	public void paintScreen() {
		if (!getBufferStrategy().contentsLost())
			getBufferStrategy().show();
	}

	@Override
	public void tearDown() {
		//N�o � realmente necess�rio, pois o jogo acaba.
		//Mas se fosse um fim de fase, seria.
		ball = null;
	}
	
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				BallFrame bf = new BallFrame();
				bf.setVisible(true);
				bf.startMainLoop();				
			}
		});
	}
}
