

function Maciora(ww, hh, max )
	{
	
	this.w=ww;
	this.h=hh;
	this.max = max;
	this.m = new Array(max);
	this.b = new Array(max);
	var i,j;
		this.ustawxy = function( x, y,  v)
		{
                         this.m[x + y*this.w ] = v;
		}
	
		
	this.transpozycja = function()
	{
		var w = this.w;
		var h = this.h;
		
             var mmm = new Maciora(h,w,w*h);
		

	//	this.pokaz();
	//	console.log(this.gett(0,2));

             for(i = 0 ; i < w ; i ++ )
             {
                     for(j = 0 ; j < h ; j ++ )
                     {
                          mmm.ustawxy( j, i , this.getxy(i,j) );
		//	console.log(this.get(i,j));
                     }
             }
	     // mmm.pokaz();
             this.klon(mmm);
        }
		
		
		this.getxy = function(i, j)
		{
			return this.m[i + j*this.w];
		}
		
		
		
		this.klon = function(M)
		{
			
			this.w = M.w;
			this.h = M.h;
		
			var w = this.w;
			var h = this.h;
			var m = this.m;
			for( i=0;i<w;i++)
				for( j=0;j<h;j++)
				{
					m[i+j*w] = M.m[i+j*w];
				}

			this.w = w;
			this.h = h;
			
		}	
		
		
		
		this.pokaz = function()
		{
			var h = this.h;
			var w = this.w;
			var m = this.m;
			console.log("");
			var s="";
			for(  j=0;j<h;j++)
			{
				s="";
				for(  i=0;i<w;i++)
				{
					s+= this.m[i+j*this.w] + " ";
					//System.out.pr (  m[i+j*w] + " " );  
				}
			console.log(s);
			}
			console.log("");
		}
		
		
		
		this.swap = function(m)
		{
			
			var dd;
			dd = this.m;
			this.m = m.m;
			m.m = dd;
			
			var tt;
			
			tt = this.w;
			this.w = m.w;
			m.w = tt;
			
			tt = this.h;
			this.h = m.h;
			m.h = tt;
			
			
			
		}
		
			
			
			
			
		
		
		this.mull = function(m)
		{
			var max = this.max;
			var b = this.b;
			
			
			for( i=0;i<max;i++)
					b[ i ] = 0;


			
			for( i=0 ; i < this.w ; i++)
			{
				for( j = 0 ; j < this.h ; j++)
				{
					for( k = 0 ; k < m.w ; k++)
					{
						b[ k + j*m.w ] += this.m[ i + j*this.w ] * m.m[ k + m.w*i ] ;
					}
				}
			}
			
			this.w = m.w;
			
			for(  i=0;i<this.w;i++)
				for(  j=0;j<this.h;j++)
					this.m[i+j*this.w] = b[i+j*this.w];

			
			
			
		}
		
		
		
		this.mulp = function(m)
		{


			var max = this.max;
		
			var b = this.b;
			var k;
					
					for( i=0;i<max;i++)
						b[ i ] = 0;
		
					
					this.swap(m);
		
				
				for( i=0 ; i < this.w ; i++)
				{
					for( j = 0 ; j < this.h ; j++)
					{
						for( k = 0 ; k < m.w ; k++)
						{
							b[ k + j*m.w ] += this.m[ i + j*this.w ] * m.m[ k + m.w*i ] ;
						}
					}
				}
				
			
				this.swap(m);
			
				
	
				this.h = m.h;
				
				for( i=0;i<this.w;i++)
					for( j=0;j<this.h;j++)
						this.m[i+j*this.w] = b[i+j*this.w];
				
	
					
			
		}
		
		
		
		this.sin = function(a)
		{
			return Math.sin(a);
		}
		
		this.cos = function( a)
		{
			return Math.cos(a);
		}
		
		this.tan = function( a)
		{
			return Math.tan(a);
		}
		
		this.przypisz = function(   o00,  o01,  o02,  o03,  o10,  o11,  o12 ,  o13,  o20,  o21,  o22,  o23,  o30,  o31,  o32 ,  o33 )
        {
           //  GL * m = new GL [16];
		var m = this.m;
             m[0] = o00;
             m[1] = o01;
             m[2] = o02;
             m[3] = o03;
             
             m[4] = o10;
             m[5] = o11;
             m[6] = o12;
             m[7] = o13;
             
             m[8] = o20;
             m[9] = o21;
             m[10] = o22;
             m[11] = o23;
             
             m[12] = o30;
             m[13] = o31;
             m[14] = o32;
             m[15] = o33;
        }
		
		
	this.jeden = function()
        {
                 
                 this.przypisz( 
                 1.0, 0.0, 0.0, 0.0,
                 0.0, 1.0, 0.0, 0.0,
                 0.0, 0.0, 1.0, 0.0,
                 0.0, 0.0, 0.0, 1.0
                 );
                 

        }
		
		// podane w stopniach
         this.rot_X_deg = function(  a)
         {
                  a = (a * 3.15 / 180);
         
         
                  this.przypisz(
                  1.0, 0.0, 0.0, 0.0, 
                  0.0, this.cos(a), -this.sin(a), 0.0,
                  0.0, this.sin(a), this.cos(a), 0.0,
                  0.0, 0.0, 0.0, 1.0
                  );
                //  return m;
         }
         
         
         
         
         
         /// macierze obrotu Y
         // podane w stopniach
         this.rot_Y_deg = function(a)
         {
                  a = (a * 3.15 / 180);
                  
                  this.przypisz(
                  this.cos(a), 0.0, this.sin(a), 0.0, 
                  0.0, 1.0, 0.0 , 0.0,
                  -this.sin(a), 0.0 , this.cos(a), 0.0,
                  0.0, 0.0, 0.0, 1.0
                  );
         }
         
         
         
         
         
         /// macierze obrotu Z
         // podane w stopniach
         this.rot_Z_deg = function(  a)
         {
                  a = (a * 3.15 / 180);
         
                  this.przypisz(
                  this.cos(a), -this.sin(a), 0.0, 0.0, 
                  this.sin(a), this.cos(a), 0.0 , 0.0,
                  0.0, 0.0 , 1.0, 0.0,
                  0.0, 0.0, 0.0, 1.0
                  );
         //         return m;
         }
         
         
         this.translacja = function(  x,   y,   z)
         {
              this.przypisz(
              1.0,0.0,0.0,x,
              0.0,1.0,0.0,y,
              0.0,0.0,1.0,z,
              0.0,0.0,0.0,1.0
              );
         }
         
         
         
         
         this.skala = function(  x,   y,   z)
         {
              this.przypisz(
              x,    0.0, 0.0, 0.0,
              0.0, y,    0.0, 0.0,
              0.0, 0.0, z,    0.0,
              0.0, 0.0, 0.0, 1.0
              );
         }
         
         this.perspektywa = function(   a,   n,   f)
         {
              a = (a * 3.1415 / 180);
              this.przypisz(
              1.0 / this.tan(a/2), 0.0,           0.0,        0.0,
              0.0,           1.0 / this.tan(a/2), 0.0,        0.0,
              0.0,           0.0,           (n+f)/(n-f), (2*n*f)/(n-f),
              0.0,           0.0,           -1.0,       0.0
              );    
         }
          
         
         this.punkt = function(  x,   y,   z)
         {
		var m = this.m;
        	 m[0] = x;
        	 m[1] = y;
        	 m[2] = z;
        	 m[3] = 1;
         }
         
		
         
           this.getx = function()
         {
        	 return  this.m[0];
         }
         
           this.gety = function()
         {
        	 return  this.m[1];
         }
         
           this.getz = function()
         {
        	 return  this.m[2];
         }
         
         
         this.rz = function()
         {
        	 this.m[1] *= -1;
         }
         
         
         this.na_ekranie = function(  w,   h)
         {

		
		
        	 var m = this.m;
	

        	 if( m[2] < 0)
        		 return false;
        	 
        	 if( m[0] >=0 && m[1] >= 0 && m[0] < w && m[1] < h)
        		 return true;
        	 return false;
        	 
         }
         
         
         this.na_ekran = function(  w,   h)
         {
        	 var m = this.m;
        	 w = Math.min(w, h);
        	 m[0] = m[0] / m[2] * w + w/2;
        	 m[1] = m[1] / m[2] * w + w/2; 
         }
         
         
         this.get = function(  i)
         {
        	 return this.m[i];
         }
         
         
		
		
}
