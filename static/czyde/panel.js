
function Panel(okno)
{

		this.kamera = new Maciora(4,4,16);
		this.kamera.jeden();
		this.swiat = new Maciora(4,4,16);
		
		this.rysuje = 0;
		
		
		this.rot=0;
		this.px=0;
		this.py=0;
		this.pz=4;
		this.dx=0;
		this.dz=0;
		this.dy=0;
		this.cx=0;
		this.cy=0;

		this.maciory = new Stos();
		

		
		this.projekcja  = new Maciora(4,4,16);
		this.projekcja.perspektywa(90,0.1, 100);
		
		this.ekran = new Maciora(4,4,16);

		//projekcja.pokaz();
		this.kosc = new dane_obj("kostka.obj");
		this.morda = new dane_obj("malpa.obj");
		this.superman = new dane_obj("superman.obj");
	//	this.pilka = new dane_obj("http://localhost/czyde/pilka.obj");
	
		this.p1 = new Maciora(1,4,4);
		this.p2 = new Maciora(1,4,4);
		this.myk = new Maciora(4,4,16);
		this.tmp = new Maciora(4,4,16);
		
		this.ekran.translacja(10, 0, -100);
		this.myk.skala(10, 10, 1);
	//	ekran.mull(myk);
		
		this.cx = 0;
		this.cy = 0;
		
		this.img = okno;


		this.width = this.img.w;
		this.height = this.img.h;
		
		//console.log(this.rot);
	







/*

	dane_obj kosc;
	dane_obj superman;
	Maciora kamera;
	Maciora swiat;
	Maciora projekcja;
	Maciora ekran;
	Maciora p1, p2;
	Maciora myk;
	int width;
	int height;
	Maciora tmp;
	
	float dx, dz, dy;
	
	
	int rysuje;
	
	float cx, cy;
	
	
	float px,py,pz;
	
	float rot;
	
	BufferedImage img;
	
*/
	
	this.apdejt = function(k, mx, my) // throws AWTException
	{
	
		if( k[ 87 ] == 1)
		{
			this.pz += this.dz;
			this.px -= this.dx;
			this.py += this.dy;
		}

		
		
		if( k[83] == 1)
		{
			this.pz -= this.dz;
			this.px += this.dx;
			this.py -= this.dy;		
		}
		
		
		 if( k[68] == 1 )
	     {
	         this.px-= this.dz;
	         this.pz-= this.dx;
	     }
	     
	     if( k[65] == 1 )
	     {
	         this.px += this.dz;
	         this.pz += this.dx;
	     }
	     
	     if( k[81] == 1 )
	     {
	    	 this.py += 0.1;
	     }
	     if( k[69] == 1)
	     {
	    	 this.py -= 0.1;
	     }
	     
	     this.rot++;
		
	     this.width = this.img.w;
	     this.height = this.img.h;
				
	     var sx = this.width /2; 
	     var sy = this.height /2;
				
			

				
	     this.cx += (sy - my)/500.0;
	     this.cy -= (sx - mx)/500.0;
				
	   //  Robot r = new Robot();
	   //  r.mouseMove( sx , sy);
				
				
				
	   if( k == 27)
		return true;
	   return false;
	
		
	}
	

	
	
	this.paint = function(g)
	{

		okno.clear();

		var kamera = this.kamera;
		var myk = this.myk;
		var swiat = this.swiat;
		var tmp = this.tmp;
	

		kamera.jeden();


		myk.rot_X_deg(-this.cx);


				
			


		this.dy = -myk.get(6) / 10;
		
		myk.transpozycja();
		

		


		kamera.mull(myk);




		

		
		
		myk.rot_Y_deg(-this.cy);
		
		this.dx = myk.get(2) / 10;
		this.dz = -myk.get(10) / 10;
		

		myk.transpozycja();
		
		kamera.mull(myk);
		


		
		myk.translacja(-this.px,-this.py,-this.pz);
		kamera.mull(myk);
		
		

		swiat.jeden();
		
		myk.translacja(0, 0, -5.0);
		swiat.mull(myk);
		myk.skala(1 ,1 ,1);
		swiat.mull(myk);
		
		
		
		
	
		this.rysuje = 1;
		
		var ind; 
	
		
		
		
		
		//System.out.println(rot);
		
		tmp.jeden();
		
		tmp.mulp(kamera);
		
		tmp.mull(swiat);
	//	tmp.pokaz();		
		tmp.mulp(this.projekcja);
	//	tmp.pokaz();
		

		myk.rot_Y_deg(this.rot);
		tmp.mull(myk);

		this.maciory.push( tmp );		



		myk.rot_X_deg(this.rot);
		tmp.mull(myk);	

		myk.translacja(0,-4,0);
		tmp.mull(myk);

		
	
		this.kosc.pokaz_linie(tmp, this.img, "#00FF00");


		tmp = this.maciory.pop();

		this.maciory.push( tmp );



		myk.rot_X_deg(this.rot);
		tmp.mull(myk);

		myk.translacja(0,10,0);
		tmp.mull(myk);



		// this.superman.pokaz_linie(tmp, this.img, "#ff670b");


		tmp = this.maciory.pop();

		

	
		this.morda.pokaz_linie(tmp, this.img, "#754719");

		
		//tmp.pokaz();

	//	kamera.pokaz();

		myk.translacja(3, 0, 0);
		tmp.mull(myk);

		
		this.maciory.push(tmp);

			
		myk.rot_Z_deg(this.rot*2);
		tmp.mull(myk);
	
		this.kosc.pokaz_linie(tmp, this.img, "#0000FF");

		tmp = this.maciory.pop();
		
		myk.translacja(-6, 0, 0);
		tmp.mull(myk);


		this.maciory.push(tmp);

			
		myk.rot_Z_deg(-this.rot*2);
		tmp.mull(myk);
	
		this.kosc.pokaz_linie(tmp, this.img, "#FF0000");

		tmp = this.maciory.pop();		
		
		this.rysuje = 0;
	}

}
