package graficos;

import java.awt.Canvas;
import java.awt.Dimension;
import javax.swing.JFrame;

public class Game extends Canvas implements Runnable{ 
	
	public static JFrame frame;
	private Thread thread;
	//game rodando       v
	private boolean isRunning = true;
	private final int WIDTH = 160;
	private final int HEIGHT = 120;
	private final int SCALE = 3;
	
	public Game()
	{
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
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
		
	}
	
	public static void main(String args[]) 
	{
		Game game = new Game();
		game.start();
	}
	
	public void tick()
	{
		
	}
	
	//renderizar os graficos
	public void render()
	{
		
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
		
	}
}
