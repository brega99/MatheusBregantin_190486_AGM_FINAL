package com.mygdx.game.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;

public class Jogo extends ApplicationAdapter {

	private SpriteBatch batch;

	// variaveis que vai guarda as texturas
	private Texture[] passaros;
	private Texture fundo;

	private Texture[] canoAlto;//arrey de canos de cima
	private Texture[] canoBaixo;//arrey de canos de baixo


	// variaveis que vão guardar a altura e a largura do dispositivo
	private float larguradispositivo;
	private float alturadispositivo;

	// variaveis que vão guardar  a movimentção sendo ela no eixo x ou y
	private int movimentaX = 0;
	private int movimentaY = 0;

	private float variacao = 0;
	private float gravidade = 0;
	private float posicaoInicialVerticalPassaro = 0;

	private float alturaEnd = 0;
	private float endposicaotela = 0;


	@Override
	public void create() {
		batch = new SpriteBatch();

		fundo = new Texture("fundo.png");//pegando a textura para criar
		passaros = new Texture[3];
		passaros[0] = new Texture("passaro1.png");
		passaros[1] = new Texture("passaro2.png");
		passaros[2] = new Texture("passaro3.png");

		canoAlto = new Texture[2];// pegando texturas dos canos
		canoAlto[0] = new Texture("cano_topo.png");
		canoAlto[1] = new Texture("cano_topo_maior.png");

		canoBaixo = new Texture[2];//pegando texturas dos canos
		canoBaixo[0] = new Texture("cano_baixo.png");
		canoBaixo[1] = new Texture("cano_baixo_maior.png");


		alturadispositivo = Gdx.graphics.getHeight();// declarando que altura e e a mesma do dispositivo
		larguradispositivo = Gdx.graphics.getWidth();// declarando que largura e e a mesma do dispositivo
		posicaoInicialVerticalPassaro = alturadispositivo / 2;
		alturaEnd = alturadispositivo - posicaoInicialVerticalPassaro / 2;// para achar altura do cano de cima
		endposicaotela = (larguradispositivo / 2) * 2;// para achar a estremidade direita da tela


	}

	@Override
	public void render() {
		batch.begin();


		if (variacao > 3) // variação para animação do passaro
		{
			variacao = 0;
		}
		boolean toqueTela = Gdx.input.justTouched();// bool pra verificar toque
		if (Gdx.input.justTouched()) {
			gravidade = -25;

		}
		if (posicaoInicialVerticalPassaro > 0 || toqueTela) {
			posicaoInicialVerticalPassaro = posicaoInicialVerticalPassaro - gravidade;
		}

		batch.draw(fundo, 0, 0, larguradispositivo, alturadispositivo);// rederizando o fundo do game na cena

		canos();


		batch.draw(passaros[(int) variacao], 50, posicaoInicialVerticalPassaro);// rederizando o passaro na cena
		variacao += Gdx.graphics.getDeltaTime() * 10;

		gravidade++;
		movimentaY++;// fazendo se mover para frente na cena quando inicia a aplicação
		movimentaX++;// fazendo se mover para cima na cena quando inicia a aplicação

		batch.end();

	}

	@Override
	public void dispose() {

	}

	void canos() {

		batch.draw(canoAlto[0], endposicaotela - movimentaX, alturaEnd - 100, 100, 900);// desenhando os canos na tela tela com movimentação
		batch.draw(canoBaixo[0], endposicaotela - movimentaX, 0, 100, 900);// desenhando os canos na tela com movimentação


	}

}