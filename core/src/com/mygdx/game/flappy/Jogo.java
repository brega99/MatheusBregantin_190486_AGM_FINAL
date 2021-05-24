package com.mygdx.game.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
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
	//para guardar as  texturas  que vão ser renderizadas
	private Texture[] passaros;
	private Texture fundo;
	private Texture canoAlto;
	private Texture canoBaixo;

	BitmapFont textPontuacao;// para mostrar texto de pontos

	private boolean passouCano = false;

	private Random random;// var para random

	private int pontos = 0;// var de pontos
	private int gravidade = 0;// var de gravidade

	private float variacao = 0; // variação da animação
	private float posicaoInicialVerticalPassaro ;// posição do passaro  na vertical
	private float posicaoCanoHorizontal; //posição cano horizontal
	private float posicaoCanoVertical; //posição cano vertical
	private float larguradispositivo;// a largura do dispositivo
	private float alturadispositivo;// a altura do dispositivo
	private float espacoEntreCanos;// espaço entre os canos


	private ShapeRenderer shapeRenderer;
	//  vars de colisão
	private Circle circuloPassaro;
	private Rectangle retaguloCanoCima;
	private Rectangle retanguloBaixo;


	@Override
	public void create() {
		inicializarObjetos();// metodo para inicializar os objetos
		inicializaTexuras();// metodo para inicializar as texturas

	}

	private void inicializarObjetos() {
		random = new Random();//random
		batch = new SpriteBatch();

		alturadispositivo = Gdx.graphics.getHeight();// declarando que altura e e a mesma do dispositivo
		larguradispositivo = Gdx.graphics.getWidth();// declarando que largura e e a mesma do dispositivo
		posicaoInicialVerticalPassaro = alturadispositivo / 2;
		posicaoCanoHorizontal = larguradispositivo;
		espacoEntreCanos = 350;

		textPontuacao = new BitmapFont();// pegando a texto de pontos
		textPontuacao.setColor(Color.WHITE);// deixando ele com  cor branca
		textPontuacao.getData().setScale(10);// determinado tamanho do texto
		// pegando as colisões
		circuloPassaro = new Circle();
		retaguloCanoCima = new Rectangle();
		retanguloBaixo = new Rectangle();
		shapeRenderer = new ShapeRenderer();

	}

	private void inicializaTexuras() {

		fundo = new Texture("fundo.png");//pegando a textura para criar

		// pondo as pngs do passaro em um arrey de texturas para que forme animação na cena
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");
		// pegando texturas do cano
		canoAlto = new Texture("cano_topo.png");
		canoBaixo = new Texture("cano_baixo.png");


	}

	@Override
	public void render() {

		verificaEstadojogo();// verificando estados do jogo
		desenharTexturas();// renderizando as texuras ou desenhando na cena
		detectarColisao();// detectar as colisões  dos objetos na cena
		validarPontos();// validando pontos quando passsa entre os canos


	}

	private void detectarColisao() {

		circuloPassaro.set(50 + passaros[0].getWidth(),
				posicaoInicialVerticalPassaro + passaros[0].getHeight(),
				passaros[0].getWidth() / 2);// pondo colisao no passaro

		retanguloBaixo.set(posicaoCanoHorizontal, alturadispositivo / 2 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoBaixo.getWidth(), canoBaixo.getHeight());// pondo colisão nos canos

		retaguloCanoCima.set(posicaoCanoHorizontal, alturadispositivo / 2 - canoAlto.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical, canoAlto.getWidth(), canoAlto.getHeight());// pondo colisão nos canos

		boolean bateuCanoCima = Intersector.overlaps(circuloPassaro, retaguloCanoCima);// verificando a colisão entre cano e passaro
		boolean bateuCanoBaixo = Intersector.overlaps(circuloPassaro, retanguloBaixo);// verificando a colisão entre cano e passaro
		if (bateuCanoBaixo || bateuCanoCima) {
			Gdx.app.log("Log", "colideu");
		}
	}

	private void validarPontos() {
		if (posicaoCanoVertical < 50 - passaros[0].getWidth()) {// se passar pelo cano
			if (!passouCano) {// for diferente passouCano
				pontos++; // soma pontos
				passouCano = true;// e passouCano é verdadeiro
			}

		}

	}

	private void verificaEstadojogo() {
		posicaoCanoHorizontal -= Gdx.graphics.getDeltaTime() * 300;// velocidade do cano
		if (posicaoCanoHorizontal < -canoBaixo.getHeight()) {
			posicaoCanoHorizontal = larguradispositivo; // posição cano na horrizontal é igual a largura
			posicaoCanoVertical = random.nextInt(400) - 200;// posição vertical fica randomicamente mudando de valores
			passouCano = false;
		}
		boolean toqueTela = Gdx.input.justTouched();// bool pra verificar toque
		if (Gdx.input.justTouched()) {
			gravidade = -25;// aplicando gravidade

		}
		if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}

		variacao += Gdx.graphics.getDeltaTime() * 10;// velocidade da variação de pngs do passaro para a animação

		if (variacao > 3) // variação para animação do passaro
		{
			variacao = 0; // determinado que sera 0
		}

		gravidade++;// almentando a gravidade

	}

	private void desenharTexturas() {
		batch.begin();// coemeçando

		batch.draw(fundo, 0, 0, larguradispositivo, alturadispositivo);// rederizando o fundo do game na cena
		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);// rederizando o passaro na cena
		batch.draw(canoBaixo, posicaoCanoHorizontal, alturadispositivo / 4 - canoBaixo.getHeight() - espacoEntreCanos / 2 + posicaoCanoVertical,canoBaixo.getWidth(),canoBaixo.getHeight()+500);//renderizando cano na cena e calculando conforme o tamanho da tela
		batch.draw(canoAlto, posicaoCanoHorizontal, alturadispositivo / 2 + espacoEntreCanos / 2 + posicaoCanoVertical,canoAlto.getWidth(),canoAlto.getHeight()+500);//renderizando cano na cena e calculando conforme o tamanho da tela
		textPontuacao.draw(batch, String.valueOf(pontos), larguradispositivo / 2, alturadispositivo - 100);// renderizando os pontos na tela  cada vez que passar entre os canos

		batch.end();// terminado

	}

	@Override
	public void dispose() {

	}


}
