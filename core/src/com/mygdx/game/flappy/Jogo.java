package com.mygdx.game.flappy;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.ScreenUtils;

public class Jogo extends ApplicationAdapter {
	private SpriteBatch batch;
	private Texture passaro;
	private Texture fundo;

	//criação das variaveis de posicionamento do cenario
	private float larguraDispositivo;
	private float alturaDispositivo;

	//criação das variaveis de movimento do passaro
	private int movimentaY = 0;
	private int movimentaX = 0;

	@Override
	//objetos criados na inicialização do aplicativo
	public void create () {
		batch = new SpriteBatch();
		fundo = new Texture("fundo.png");
		passaro = new Texture("passaro1.png");

		//inserção das variaveis dentro da biblioteca GDX para declara-las como altura de largura
		larguraDispositivo = Gdx.graphics.getWidth();
		alturaDispositivo = Gdx.graphics.getHeight();
	}

	@Override
	public void render () {
		batch.begin();

		//atribuir caracteristicas da sprite do cenário de fundo
		batch.draw(fundo, 0, 0, larguraDispositivo, alturaDispositivo);

		//atribuir caracteristicas da sprite do passaro/ ex: tamanho e movimento
		batch.draw(passaro, movimentaX, movimentaY,220 , 150);

		movimentaX++;
		movimentaY++;

		batch.end();
	}

	@Override
	public void dispose () {

	}
}