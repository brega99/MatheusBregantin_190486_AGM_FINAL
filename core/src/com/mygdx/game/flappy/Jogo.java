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
	private Texture passaros;
	private Texture fundo;
	private Texture canoAlto;
	private Texture canoBaixo;
	private Texture GameOver;
	private Texture moedadeprata;
	private Texture moedadeouro;
	private Texture logo;


	BitmapFont textPontuacao;// Mostra o texto de pontuação
	BitmapFont textRenicia; // Texto de reiniciar
	BitmapFont textMelhorPontuacao;// Texto de melhor pontuação

	private boolean passouCano = false;// Verifica se passou pelo cano

	private Random random;// var para random

	private int pontuacaoMaxima = 0; // Var pontuação maxima
	private int pontos = 0; // Var de pontos
	private int gravidade = 0; // Var de gravidade
	private int estadojogo = 0; // Var de estado de jogo
	private int moedapravalor = 0;
	int valor = 1;

	private float variacao = 0; // Variação de animação
	private float posicaoInicialVerticalPassaro = 0; // Posição do player  na vertical
	private float posicaoCanoHorizontal; // Posição do cano horizontal
	private float posicaoCanoVertical; // Posição do cano vertical
	private float larguradispositivo; // A largura do dispositivo
	private float alturadispositivo; // A altura do dispositivo
	private float espacoEntreCanos; // Espaço entre os canos
	private float posicaoHorizontalPassaro = 0; // Posição na horizontal do passaro
	private float posicaoMOedaouro; // Posição moeda de ouro
	private float posicaoMOedaPrata; // Posição da moeda de prata
	private float posicaomoedavetical; // Posição das moedas na vertical


	private ShapeRenderer shapeRenderer;
	// Vars de colisão
	private Circle circuloPassaro;
	private Rectangle retanguloCanoCima;
	private Rectangle retanguloBaixo;
	private Circle ciculoMoedaOuro;
	private Circle ciculoMoedaPrata;

	// Sons aplicados no game
	Sound somVoando;
	Sound somColisao;
	Sound somPontuacao;
	Sound somMoedas;

	Preferences preferencias;



	@Override
	public void create() {
		inicializaTexuras(); // Metodo de inicialização dos objetos
		inicializarObjetos(); // Metodo de incialização das texturas


	}

	private void inicializarObjetos() {

		batch = new SpriteBatch();
		random = new Random(); // Random


		alturadispositivo = Gdx.graphics.getHeight(); // Declara que a altura do dispositivo é a mesma
		larguradispositivo = Gdx.graphics.getWidth(); // Declara que a largura do dispositivo é a mesma
		posicaoInicialVerticalPassaro = alturadispositivo / 2; // Iguala posição vertical do passaro com altura do dispositivo
		posicaomoedavetical = alturadispositivo / 2; // Iguala a posição na vertical da moeda, com a altura do dispositivo
		posicaoCanoHorizontal = larguradispositivo; // Iguala a posição do cano horizontal com a largura do dipositivo
		posicaoMOedaouro = larguradispositivo; // Iguala posição da moeda de ouro com a largura do dispositivo
		posicaoMOedaPrata = larguradispositivo; // Iguala posição da moeda de prata com a largura do dispositivo

		espacoEntreCanos = 350; //Espaço entre os canos


		textPontuacao = new BitmapFont(); // Coleta o texto de pontos
		textPontuacao.setColor(Color.WHITE); // Transformando o texto na cor branca
		textRenicia = new BitmapFont(); // Coleta o texto de pontos
		textPontuacao.getData().setScale(10); // Determina o tamanho do texto

		textRenicia.setColor(Color.GREEN); // Transformando o texto na cor verde
		textRenicia.getData().setScale(3); // Determina o tamanho do texto

		textMelhorPontuacao = new BitmapFont(); // Coleta o texto de pontos
		textMelhorPontuacao.setColor(Color.RED); // Transformando o texto na cor vermelho
		textMelhorPontuacao.getData().setScale(3); // Determina o tamanho do texto


		// Colisões
		shapeRenderer = new ShapeRenderer();
		circuloPassaro = new Circle();
		retanguloCanoCima = new Rectangle();
		retanguloBaixo = new Rectangle();
		ciculoMoedaOuro = new Circle();
		ciculoMoedaPrata = new Circle();

		//Coletando arquivos de som
		somVoando = Gdx.audio.newSound(Gdx.files.internal("som_asa.wav"));
		somColisao = Gdx.audio.newSound(Gdx.files.internal("som_batida.wav"));
		somPontuacao = Gdx.audio.newSound(Gdx.files.internal("som_pontos.wav"));
		somMoedas = Gdx.audio.newSound(Gdx.files.internal("audiocoin.mp3"));


		preferencias = Gdx.app.getPreferences("flappyBird");
		pontuacaoMaxima = preferencias.getInteger("pontuacaoMaxima", 0);// para gardar maior pontuação

	}

	private void inicializaTexuras() {

		fundo = new Texture("fundo.png"); // Coletando a textura para criação

		// Colocando as imagens do passaro em um arrey de texturas para que a animação se forme em cena
		passaros = new Texture("bosta.png");
		// Pegando texturas do cano
		canoAlto = new Texture("cano_topo_maior.png");
		canoBaixo = new Texture("cano_baixo_maior.png");
		// Pegando arquivos de imagem das moedas
		moedadeouro = new Texture("ouro.png");
		moedadeprata = new Texture("prata.png");
		// Pegando arquivos de imagem de game over e logo
		GameOver = new Texture("game_over.png");
		logo = new Texture("Logothelast.png");


	}

	@Override
	public void render() {

		verificaEstadojogo(); // Verificação de estados do jogo
		desenharTexturas(); // Render das texuras
		detectarColisao(); // Detecção das colisões dos objetos em cena
		validarPontos(); // Validação de pontos quando o player passa entre os obstaculos


	}

	private void detectarColisao() {

		circuloPassaro.set(50 + passaros.getWidth() / 2f,
				posicaoInicialVerticalPassaro + passaros.getHeight() / 2f,
				passaros.getWidth() / 2f);// pondo colisao no passaro

		retanguloBaixo.set(posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight()); // Colisão dos canos de baixo

		retanguloCanoCima.set(posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth(), canoAlto.getHeight()); // Colisão dos canos de cima

		ciculoMoedaPrata.set(posicaoMOedaPrata, alturadispositivo /2 + posicaomoedavetical + moedadeprata.getHeight() / 2f, // Colisão Moeda de prata
				moedadeprata.getWidth() / 2f);

		ciculoMoedaOuro.set(posicaoMOedaouro, alturadispositivo /2 + posicaomoedavetical + moedadeouro.getHeight() / 2f, // Colisão Moeda de ouro
				moedadeouro.getWidth() / 2f);

		boolean beteumoedaOuro = Intersector.overlaps(circuloPassaro, ciculoMoedaOuro); // Verificação da colisão entre o passaro (player) e a moeda de ouro
		boolean beteumoedaPrata = Intersector.overlaps(circuloPassaro, ciculoMoedaPrata); // Verificação da colisão entre o passaro (player) e a moeda de prata
		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retanguloCanoCima); // Verificação da colisão entre o passaro (player) e o cano
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloBaixo); // Verificação da colisão entre o passaro (player) e o cano
		if (bateuCanoBaixo || bateuCanoCima) {
			// Caso o estado do jogo for 1 dispara o som de colisão e modifica o estado do jogo para 2
			if (estadojogo == 1) {
				somColisao.play(); // Dispara o som da colisão
				estadojogo = 2; // Estado do game

			}
		}
		if (beteumoedaOuro) {
			if (estadojogo == 1) {
				pontos += 10;
				moedapravalor = 0;
				somMoedas.play();
				posicaoMOedaouro = larguradispositivo;

			}
		}
		if (beteumoedaPrata) {

			if (estadojogo == 1) {
				pontos += 5;
				moedapravalor++;
				somMoedas.play();
				posicaoMOedaPrata = larguradispositivo;

			}
		}
	}

	private void validarPontos() {
		if (posicaoCanoHorizontal < 50 - passaros.getWidth()) {
			// Caso passe pelo cano
			if (!passouCano) { // for diferente de passouCano
				pontos++; // Soma de pontos
				passouCano = true; // passouCano é true
				somPontuacao.play(); //playsom


			}

		}

	}

	private void verificaEstadojogo() {

		boolean toqueTela = Gdx.input.justTouched(); // Bool pra verificar o toque
		// Estado  0 , após tocar na tela o player vai para a parte inferior e muda estado do jogo pra 1 disparando o som de voo
		if (estadojogo == 0) {
			if (Gdx.input.justTouched()) {
				gravidade = -15;
				estadojogo = 1;
				somVoando.play();
			}

		}
		// Estado do jogo em 1,ativa gravidade e dispara som de voo, e faz com que o cano comece a se movimentar
		else if (estadojogo == 1) {
			valor = 0;
			if (toqueTela) {
				gravidade = -15;
				somVoando.play();
			}
			posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 200; // Velocidade dos cano
			if (posicaoCanoHorizontal < -canoBaixo.getWidth()) {
				posicaoCanoHorizontal = larguradispositivo; // Posição do cano horizontal é = largura
				posicaoCanoVertical = random.nextInt(400) - 200; // A posição vertical muda de valores randomicamente
				passouCano = false;
			}
			posicaoMOedaPrata -= Gdx.graphics.getDeltaTime() * 150;
			if (posicaoMOedaPrata < -moedadeprata.getWidth()) {
				posicaoMOedaPrata = larguradispositivo;
				posicaomoedavetical = random.nextInt(300) - 200;

			}
			if (moedapravalor >= 5) {
				posicaoMOedaouro -= Gdx.graphics.getDeltaTime() * 150;
				if (posicaoMOedaouro < -moedadeouro.getWidth()) {
					posicaoMOedaouro = larguradispositivo;
					posicaomoedavetical = random.nextInt(300) - 200;
					moedapravalor = 0;
				}
			}

			if (posicaoInicialVerticalPassaro > 0 || toqueTela)
				posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;


			gravidade++; // Aumento da gravidade


		}
		// estado do jogo em 2 , faz quando se colide com o cano, mostra melhor pontuação e se o jogaddor clicar na tela recomeça o jogo
		else if (estadojogo == 2) {
			if (pontos > pontuacaoMaxima) // Quando a nova pontuação for maior que a pontuação maxima,  pontuação maxima será subistituida pela nova
			{
				pontuacaoMaxima = pontos;
				preferencias.putInteger("pontuacaoMaxima", pontuacaoMaxima);
			}

			posicaoHorizontalPassaro -= Gdx.graphics.getDeltaTime() * 500; // Efeito de colisão, quando for o caso

			if (toqueTela) // Reset do game
			{
				estadojogo = 0; // estado de jogo
				pontos = 0; // Numero de pontuação
				gravidade = 0; // Valor da gravidade
				posicaoHorizontalPassaro = 0; // Valor passaro naa vertical
				posicaoInicialVerticalPassaro = alturadispositivo / 2;
				posicaoCanoHorizontal = larguradispositivo;
				posicaoMOedaouro = larguradispositivo;
				posicaoMOedaPrata = larguradispositivo;
				moedapravalor = 0;
			}
		}


	}

	private void desenharTexturas() {
		batch.begin(); // Inicio

		batch.draw(fundo, 0, 0, larguradispositivo, alturadispositivo);// Render do fundo do game em cena
		if (estadojogo == 0 && valor == 1 )
		{
			batch.draw(logo,posicaoHorizontalPassaro,alturadispositivo /4,1200,800);

		}
		batch.draw(passaros, 50 + posicaoHorizontalPassaro, posicaoInicialVerticalPassaro); // Render do passaro em cena
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical); // Render cano na cena e calculando conforme o tamanho da tela
		batch.draw(canoAlto, posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical);// Render cano na cena e calculando conforme o tamanho da tela
		textPontuacao.draw(batch, String.valueOf(pontos), larguradispositivo / 2, alturadispositivo - 100); // Render de pontos na tela toda vez q o passaro passar no meio dos canos

		// Caso o estado do jogo for  2, renderizar na tela as imagens  na tela dando infomação sobre o status que o game se encontra
		if (estadojogo == 2) {
			batch.draw(GameOver, larguradispositivo / 2 - GameOver.getWidth() / 2f, alturadispositivo / 2);
			textRenicia.draw(batch, "Toque  na tela para reiniciar!", larguradispositivo / 2 - 200, alturadispositivo / 2 - GameOver.getHeight() / 2f);
			textMelhorPontuacao.draw(batch, "Sua melhor pontuação  é : " + pontuacaoMaxima + " Pontos", larguradispositivo / 2 - 300, alturadispositivo / 2 - GameOver.getHeight() * 2);
		}
		if (moedapravalor <= 5) {

			batch.draw(moedadeprata, posicaoMOedaPrata, alturadispositivo /2 + posicaomoedavetical + moedadeprata.getHeight() / 2f);
		}

		if (moedapravalor >= 5) {

			batch.draw(moedadeouro, posicaoMOedaouro, alturadispositivo /2 + posicaomoedavetical + moedadeouro.getHeight() / 2f);


		}

		batch.end(); //Final

	}

	@Override
	public void dispose() {


	}


}
