package com.mygdx.game.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Preferences;
import com.badlogic.gdx.audio.Sound;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;

import java.util.Random;


public class Jogo extends ApplicationAdapter {

	private SpriteBatch batch;
	//Para guardar as  texturas  que irão ser usadas
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoAlto;
	private Texture canoBaixo;
	private Texture GameOver;

	BitmapFont textPontuacao; // Mostra o texto de pontuação
	BitmapFont textRenicia; // Texto de reiniciar
	BitmapFont textMelhorPontuacao; // Texto de melhor pontuação

	private boolean passouCano = false;

	private Random random; // Var para random

	private int pontuacaoMaxima = 0; // Var pontuação maxima
	private int pontos = 0; // Var de pontos
	private int gravidade = 0; // Var de gravidade

	private float variacao = 0; // Variação de animação
	private float posicaoInicialVerticalPassaro;// Posição do player  na vertical
	private float posicaoCanoHorizontal; // Posição do cano horizontal
	private float posicaoCanoVertical; // Posição do cano vertical
	private float larguradispositivo; // A largura do dispositivo
	private float alturadispositivo; // A altura do dispositivo
	private float espacoEntreCanos; // Espaço entre os canos
	private int estadojogo = 0;
	private float posicaoHorizontalPassaro = 0; // Posição horizontal do player


	private ShapeRenderer shapeRenderer;
	// Vars de colisão
	private Circle circuloPassaro;
	private Rectangle retaguloCanoCima;
	private Rectangle retanguloBaixo;

	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;

	Preferences preferencias;

	@Override
	public void create() {
		inicializarObjetos(); // Metodo de inicialização dos objetos
		inicializaTexuras(); // Metodo de incialização das texturas

	}

	private void inicializarObjetos() {
		random = new Random(); // Random
		batch = new SpriteBatch();

		alturadispositivo = Gdx.graphics.getHeight(); // Declara que a altura do dispositivo é a mesma
		larguradispositivo = Gdx.graphics.getWidth(); // Declara que a largura do dispositivo é a mesma
		posicaoInicialVerticalPassaro = alturadispositivo / 2; // Iguala posição vertical do passaro com altura do dispositivo
		posicaoCanoHorizontal = larguradispositivo; // Iguala a posição do cano horizontal com a largura do dipositivo
		espacoEntreCanos = 350;

		textPontuacao = new BitmapFont();// Coleta o texto de pontos
		textPontuacao.setColor(Color.WHITE);// Transformando o texto na cor branca
		textPontuacao.getData().setScale(10);// Determina o tamanho do texto

		textRenicia = new BitmapFont();// Coleta o texto de pontos
		textRenicia.setColor(Color.GREEN);// Transformando o texto na cor verde
		textRenicia.getData().setScale(3);// Determina o tamanho do texto

		textMelhorPontuacao = new BitmapFont();// Coleta o texto de pontos
		textMelhorPontuacao.setColor(Color.RED);// Transformando o texto na cor vermelho
		textMelhorPontuacao.getData().setScale(3);// Determina o tamanho do texto

		// Colisões
		circuloPassaro = new Circle();
		retaguloCanoCima = new Rectangle();
		retanguloBaixo = new Rectangle();
		shapeRenderer = new ShapeRenderer();

		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));


		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);

	}

	private void inicializaTexuras() {

		fundo = new Texture("fundo.png");// Coletando a textura para criação

		// Colocando as imagens do passaro em um arrey de texturas para que a animação se forme em cena
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		// pegando texturas do cano
		canoAlto = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		GameOver = new Texture("game_over.png");


	}

	@Override
	public void render() {

		verificaEstadojogo();// Verificação de estados do jogo
		desenharTexturas();// Render das texuras
		detectarColisao();// Detecção das colisões dos objetos em cena
		validarPontos();// Vvalidação de pontos quando o player passa entre os obstaculos


	}

	private void detectarColisao() {

		circuloPassaro.set(50 + passaros[0].getWidth() / 2,
				posicaoInicialVerticalPassaro + passaros[0].getHeight() / 2,
				passaros[0].getWidth() / 2);// pondo colisao no passaro

		retanguloBaixo.set(posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());// Colisão dos canos de baixo

		retaguloCanoCima.set(posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth(), canoAlto.getHeight());// Colisão dos canos de cima

		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retaguloCanoCima);// Verificação da colisão entre o passaro (player) e o cano
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloBaixo);// Verificação da colisão entre o passaro (player) e o cano
		if (bateuCanoBaixo || bateuCanoCima) {
			Gdx.app.log("Log", "Colidiu");

			if (estadojogo == 1) {
				somColisao.play();
				estadojogo = 2;
			}
		}
	}

	private void validarPontos() {
		if (posicaoCanoHorizontal < 50 - passaros[0].getWidth()) {
			// Caso passe pelo cano
			if (!passouCano) { // for diferente de passouCano
				pontos++; // Soma de pontos
				passouCano = true;// passouCano é true
				somPontuacao.play(); //playsom
			}

		}
		variacao += Gdx.graphics.getDeltaTime() * 10;// Velocidade da animação

		if (variacao > 3) // Variação da animação do player
		{
			variacao = 0; // Determinando que o valor será = 0
		}

	}

	private void verificaEstadojogo() {

		boolean toqueTela = Gdx.input.justTouched();// Bool pra verificar o toque
		if (estadojogo == 0) {
			if (Gdx.input.justTouched()) {
				gravidade = -15;
				estadojogo = 1;
				somVoando.play();
			}

		} else if (estadojogo == 1) {
			if (toqueTela) {
				gravidade = -15;
				somVoando.play();
			}
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200;// Velocidade dos cano
			if (posicaoCanoHorizontal < -canoBaixo.getHeight()) {
				posicaoCanoHorizontal = larguradispositivo; // Posição do cano horizontal é = largura
				posicaoCanoVertical = random.nextInt(400) - 200;// A posição vertical muda de valores randomicamente
				passouCano = false;
			}
			if (posicaoInicialVerticalPassaro > 0 || toqueTela)
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


			gravidade++;// Aumento da gravidade

		} else if (estadojogo == 2) {
			if (pontos > pontuacaoMaxima) {
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}
			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500;
			if (toqueTela) {
				estadojogo = 0;
				pontos = 0;
				gravidade = 0;
				posicaoHorizontalPassaro = 0;
				posicaoInicialVerticalPassaro = alturadispositivo / 2;
				posicaoCanoHorizontal = larguradispositivo;
			}
		}


	}

	private void desenharTexturas() {
		batch.begin();// Começo

		batch.draw(fundo, 0, 0, larguradispositivo, alturadispositivo);// Render do fundo na cena
		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);// Render do passaro em cena (player)
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight()); //Render do cano na cena e calcular o tamanho de acordo com as porporções da tela
		batch.draw(canoAlto, posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth(), canoAlto.getHeight());//Render do cano na cena e calcular o tamanho de acordo com as porporções da tela
		textPontuacao.draw(batch, String.valueOf(pontos), larguradispositivo / 2, alturadispositivo - 100);// Render de pontos na tela todas as vezes que passar entre os canos

		if (estadojogo == 2) {
			batch.draw(GameOver, larguradispositivo / 2 - GameOver.getWidth() / 2, alturadispositivo / 2);
			textRenicia.draw(batch, "Toque  na tela para reiniciar!", larguradispositivo / 2 - 200, alturadispositivo / 2 - GameOver.getHeight() / 2);
			textMelhorPontuacao.draw(batch, "Sua melhor pontuação  é : " + pontuacaoMaxima + " Pontos", larguradispositivo / 2 - 300, alturadispositivo / 2 - GameOver.getHeight() * 2);
		}


		batch.end(); // Final

	}

	@Override
	public void dispose() {

	}


}