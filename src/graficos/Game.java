package graficos;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JFrame;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

public class Game extends Canvas implements Runnable{ 
	
	public static JFrame frame;
	private Thread thread;
	//game rodando       v
	private boolean isRunning = true;
	//Largura e Altura da tela ("constantes"):
	private final int WIDTH = 1900;
	private final int HEIGHT = 1080;
	private final int SCALE = 3;
	
	private BufferedImage image;
	
	private Spritesheet sheet;
	private BufferedImage player;
	
	
	public Game()
	{
		sheet = new Spritesheet("/spritesheet.png");
		player = sheet.getSprite(0, 0, 19, 22);
		// ^ pega a sprite do player (usando o idoso como idle)
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	}
	
	public void initFrame()
	{
		frame = new JFrame("Project Emerald");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
	
	public synchronized void start()
	{
		thread = new Thread(this);
		isRunning = true;
		thread.start();
	}
	
	public synchronized void stop()
	{
		isRunning = false;
		
		try 
		{
			thread.join();
		} catch (InterruptedException e) 
		{
			e.printStackTrace();
		}
	}
	
	public static void main(String args[]) 
	{
		Game game = new Game();
		game.start();
	}
	
	public void tick()
	{
		x++;
	}
	
	//renderizar os graficos
	public void render()
	{
		//sequencia de Buffers na tela para otimizar a renderização
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null)
		{
			//visa performance:
			this.createBufferStrategy(3);
			return;
		}
		
		Graphics g = image.getGraphics();
		g.setColor(new Color(23,255,36));
		g.fillRect(0,0,WIDTH,HEIGHT);
		g.setColor(Color.BLACK);
		g.fillRect(20, 20, 80, 80);
		//renderização do game:
		
		g.drawImage(player, x, 20, null);
		//
		g.dispose();
		g = bs.getDrawGraphics(); 
		//desenhar imagem
		g.drawImage(image,0,0,WIDTH*SCALE,HEIGHT*SCALE,null);
		bs.show();
	}
	
	public void run()
	{
		//loop de execucao, primeiro pega a hora em nanossegundos:
		long lastTime = System.nanoTime();
		//define a constante de frames por segundo:
		double amountOfTicks = 60;
		//calculo para saber o tempo certo de atualizar o jogo, isso é, cada frame:
		double ns = 1000000000 / amountOfTicks;
		
		double delta = 0;
		//contador de FPS
		int frames = 0;
		double timer = System.currentTimeMillis();
		while(isRunning)
		{
			long now = System.nanoTime();
			//diminui tempo atual pela ultima vez:
			delta+= (now - lastTime) / ns;
			lastTime = now;
			if(delta >= 1)
			{
				//sempre atualiza o jogo...
				tick();
				//...antes de renderizar o momento atual
				render();
				frames++;
				delta--;
			}
			//verifica se passou um segundo depois de ter mostrado a mensagem pela última vez
			if(System.currentTimeMillis() - timer >= 1000)
			{
				System.out.println("FPS: "+ frames);
				//reseta o numero de frames apos a contagem
				frames = 0;
				timer += 1000;
			}
		}
		
		stop();
		
	}
}
