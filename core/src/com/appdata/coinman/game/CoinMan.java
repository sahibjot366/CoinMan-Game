package com.appdata.coinman.game;

import com.badlogic.gdx.ApplicationAdapter;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.Rectangle;
import com.badlogic.gdx.utils.ScreenUtils;



import java.util.ArrayList;
import java.util.Calendar;
import java.util.Random;



public class CoinMan extends ApplicationAdapter {
	SpriteBatch batch;
	Texture background;
	Texture[] man;
	Texture coin;
	Texture bomb;
	Texture jump;
	Texture dizzle;
	Random random;
	ArrayList<Integer> coinListX;
	ArrayList<Integer> coinListY;
	ArrayList<Integer> bombListX;
	ArrayList<Integer> bombListY;
	ArrayList<Rectangle> coinRectList;
	ArrayList<Rectangle> bombRectList;
	Calendar now;
	Rectangle manRect;
	int index;
	int runSpeedControlVar;
	int coinGeneratingSpeed;
	int bombGeneratingSpeed;
	Float velocity;
	int gameState;
	Float gravity;
	int jumpTime;
	int many;
	Texture fall;
	int score;
	boolean hasJumped;
	BitmapFont font;
	int hour;
	public void makeCoin(){
		int coiny=(int)(random.nextFloat()*Gdx.graphics.getHeight());
		coinListX.add(Gdx.graphics.getWidth());
		coinListY.add(coiny);
	}
	public void makeBomb(){
		int bomby=(int)(random.nextFloat()*Gdx.graphics.getHeight());
		bombListX.add(Gdx.graphics.getWidth());
		bombListY.add(bomby);
	}
	@Override
	public void create () {
		batch = new SpriteBatch();
		now=Calendar.getInstance();
		hour=now.get(Calendar.HOUR_OF_DAY);
		background=new Texture("bg.jpg");
//		if(hour>=20 && hour<4)
//			background=new Texture("night.jpeg");
//		else if(hour>4 && hour<12)
//			background=new Texture("morning.jpeg");
//		else if(hour>=12 && hour<20)
//			background=new Texture("evening.jpeg");
		man=new Texture[4];
		man[0]=new Texture("frame-1.png");
		man[1]=new Texture("frame-2.png");
		man[2]=new Texture("frame-3.png");
		man[3]=new Texture("frame-4.png");
		coin=new Texture("dollar.png");
		dizzle=new Texture("dizzle.png");
		bomb=new Texture("bomb.png");
		index=0;
		fall=new Texture("framefall.png");
		runSpeedControlVar=0;
		random=new Random();
		coinListX=new ArrayList<Integer>();
		coinListY=new ArrayList<Integer>();
		bombListX=new ArrayList<Integer>();
		bombListY=new ArrayList<Integer>();
		coinGeneratingSpeed=0;
		bombGeneratingSpeed=0;
		velocity=0f;
		gravity=0.2f;
		jump=new Texture("frame.png");
		many=Gdx.graphics.getHeight()/2;
		hasJumped=false;
		jumpTime=0;
		coinRectList=new ArrayList<Rectangle>();
		bombRectList=new ArrayList<Rectangle>();
		font=new BitmapFont();
		font.setColor(Color.BLACK);
		font.getData().scale(3);
		score=0;
		gameState=0;
	}

	@Override
	public void render () {
		batch.begin();
		batch.draw(background,0,0, Gdx.graphics.getWidth(),Gdx.graphics.getHeight());
		if(gameState==1){
			if(Gdx.input.justTouched()){
				velocity=-10f;
				velocity+=gravity;
				many-=velocity;
				hasJumped=true;
			}
			if(runSpeedControlVar<7){
				runSpeedControlVar++;
			}
			else{
				runSpeedControlVar=0;
				if(index<3)
				{
					index++;
				}else{
					index=0;
				}
			}
			if(coinGeneratingSpeed<100)
				coinGeneratingSpeed++;
			else {
				coinGeneratingSpeed=0;
				makeCoin();
			}
			if(bombGeneratingSpeed<300)
				bombGeneratingSpeed++;
			else{
				bombGeneratingSpeed=0;
				makeBomb();
			}
			velocity+=gravity;
			if(many>=0){
				many-=velocity;
			}
			if(hasJumped){
				if(jumpTime<25)
				{
					batch.draw(jump,Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
					jumpTime++;
				}
				else{
					jumpTime=0;
					hasJumped=false;
				}
			}
			else if(many>0){
				batch.draw(fall,Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
			}
			else
				batch.draw(man[index],Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
			coinRectList.clear();
			for(int i=0;i<coinListX.size();i++){
				batch.draw(coin,coinListX.get(i),coinListY.get(i));
				coinRectList.add(new Rectangle(coinListX.get(i),coinListY.get(i),coin.getWidth(),coin.getHeight()));
				coinListX.set(i,coinListX.get(i)-4);
			}
			bombRectList.clear();
			for(int i=0;i<bombListX.size();i++){
				batch.draw(bomb,bombListX.get(i),bombListY.get(i));
				bombRectList.add(new Rectangle(bombListX.get(i),bombListY.get(i),bomb.getWidth(),bomb.getHeight()));
				bombListX.set(i,bombListX.get(i)-4);
			}
		}
		else if(gameState==0){
			batch.draw(fall,Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
			if(Gdx.input.justTouched()){
				gameState=1;
			}
		}else if(gameState==2){
			if(Gdx.input.justTouched()){
				gameState=1;
				index=0;
				score=0;
				hasJumped=false;
				jumpTime=0;
				many=Gdx.graphics.getHeight()/2;
				coinGeneratingSpeed=0;
				bombGeneratingSpeed=0;
				velocity=0f;
				runSpeedControlVar=0;
				bombRectList.clear();
				coinRectList.clear();
				coinListY.clear();
				coinListX.clear();
				bombListX.clear();
				bombListY.clear();
			}

		}
		manRect=new Rectangle(Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many,man[index].getWidth(),man[index].getHeight());
		for(int i=0;i<coinRectList.size();i++){
			if(Intersector.overlaps(manRect,coinRectList.get(i))){
				score++;
				coinRectList.remove(i);
				coinListX.remove(i);
				coinListY.remove(i);
				break;
			}
		}
		for(int i=0;i<bombRectList.size();i++){
			if(Intersector.overlaps(manRect,bombRectList.get(i))){
				gameState=2;
				batch.draw(dizzle,Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
				break;
			}
		}
		if(Gdx.graphics.getHeight()-many<=300){
			gameState=2;
			batch.draw(dizzle,Gdx.graphics.getWidth()/2-man[index].getWidth()/2-200,many);
		}
		font.draw(batch,"Score: "+String.valueOf(score),Gdx.graphics.getWidth()-1000,Gdx.graphics.getHeight()-100);
		batch.end();
	}
	
	
	@Override
	public void dispose () {
		batch.dispose();
	}
}
