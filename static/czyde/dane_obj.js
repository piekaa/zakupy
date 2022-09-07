

function dane_obj(nazwa)
{



       
       
       
       this.wierzcholki = new vector();
       this.normalne = new vector();;
       this.textury = new vector();;
       this.rys_w = new vector();;
       this.rys_n = new vector();;
       this.rys_t = new vector();;
       this.linie = new vector();;
       
       this.width;
       this.height;

       this.p1 = new Maciora(1,4,4);
       this.p2 = new Maciora(1,4,4);
       
       this.Twierzcholki = new Array();
       this.Trys_w= new Array();
       this.Trys_n= new Array();
       this.Trys_t= new Array();
       this.Tlinie= new Array();
	
       
                       


       
        this.ww=0;
	this.wrw; 
	this.wrn;
	this.wrt=0;
	this.wl=0;
       
       

		this.string_do_dabla = function( s)
		{
			if( s.length > 0)
		       return parseFloat(s);
			else
				return -1;
		}
		

		this.string_do_inta = function( s )
		{
			if( s.length > 0)
				return parseInt(s);
			else
				return -1;
		}

       
       

		this.teges = function( s)
		{
		       var n = "";
		       var ss = "";
		       var i;
		       
		       
		       
		       for( i = 0 ;   i < s.length  &&  s.charAt(i) != ' '; i++ )
		       {
		            n = n + s.charAt(i);
		       }
		       
		       i++;
		       
		       while( i < s.length )
		       {
		              ss = ss + s.charAt(i++);
		       }
		       s = ss;
		       
		       return n;
		       
		}
		
		
		this.teges_next = function(s)
		{
		       var n = "";
		       var ss = "";
		       var i;
		       
		       
		       
		       for( i = 0 ;   i < s.length  &&  s.charAt(i) != ' '; i++ )
		       {
		            n = n + s.charAt(i);
		       }
		       
		       i++;
		       
		       while( i < s.length )
		       {
		              ss = ss + s.charAt(i++);
		       }
		       s = ss;
		       
		       return s;
		       
		}
               
		
		
		
		this.string_teges = function(  s )
		{
		    var w;
		    
		    var es = ""; 
		    
		    var i;
		    
		    for( i = 0 ; i < s.length && s.charAt(i) != '/' ; i++ )
		    {
		         es = es + s.charAt(i);
		    }
		    w = this.string_do_inta ( es );
		    
		    es = "";
		    
		    for( i++ ; i < s.length ; i++ )
		    es = es + s.charAt(i);
		    
		    s = es;
		    return w;
		    
		    
		} 
		
		
		this.string_teges_next = function( s )
		{
		 
		    
		    var w;
		    
		    var es = ""; 
		    
		    var i;
		    
		    for( i = 0 ; i < s.length && s.charAt(i) != '/' ; i++ )
		    {
		         es = es + s.charAt(i);
		    }
		    w = this.string_do_inta ( es );
		    
		    es = "";
		    
		    for( i++ ; i < s.length ; i++ )
		    es = es + s.charAt(i);
		    
		    s = es;
		    return s;
		    
		    
		} 


		
		
               
               
        this.czytaj_obj = function( nazwa ) 
        {
            var s = "";
            var co = "";
	    var n;
	    var j;
   
            
            var iplik = new readFile( nazwa );
         
            
            
            
            while( iplik.hasNextLine() )
            {
            	s = iplik.nextLine();
            	
           
                   
                   co = this.teges(s);
                   s = this.teges_next(s);
                  
              //     System.out.println(s);
                   
                   if( co ==  "v"  )
                   {
                       this.wierzcholki.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                       this.wierzcholki.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                       this.wierzcholki.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                   }
                   
                   if( co == "vn"  )
                   {
                       this.normalne.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                       this.normalne.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                       this.normalne.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                   }
                   
                   if( co == "vt"  )
                   {
                       this.textury.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                       this.textury.push_back ( this.string_do_dabla( this.teges(s) ) );
                       s = this.teges_next(s);
                   }
                   
                   
                   if( co ==  "f"  )
                   {
                       for( j=0 ; j<3 ; j++)
                       {
                               var tmp =  this.teges(s) ;
                               s = this.teges_next(s);
                               
                             //  System.out.println(s);
                              
                               n = this.string_teges( tmp ) -1;
                               tmp = this.string_teges_next(tmp);
                               this.rys_w.push_back ( this.wierzcholki.fat(n*3) );
                               this.rys_w.push_back ( this.wierzcholki.fat(n*3+1) );
                               this.rys_w.push_back ( this.wierzcholki.fat(n*3+2) );
                               
                               this.linie.push_back( n );
                             //	  System.out.println(n);
                               //ind_wierzcholki[wiw++] =  n;
                               
                               
                               
                               n = this.string_teges( tmp ) -1;
                               tmp = this.string_teges_next(tmp);
                               if( n != -2 )
                               {
                                   this.rys_t.push_back( this.textury.fat(n*2) );
                                   this.rys_t.push_back( this.textury.fat(n*2+1) );
                               //ind_textury[wit++] = n;
                               }
                               
                               n = this.string_teges( tmp ) -1;
                               tmp = this.string_teges_next(tmp);
                               if( n != -2 )
                               {
                                   this.rys_n.push_back( this.normalne.fat(n*3) );
                                   this.rys_n.push_back( this.normalne.fat(n*3+1) );
                                   this.rys_n.push_back( this.normalne.fat(n*3+2) );
                               }
                               //ind_normalne[win++] = n;
                       }
                       
                       this.linie.push_back( this.linie.iat(this.linie.size()-3) );
                       this.linie.push_back( this.linie.iat(this.linie.size()-3) );
                       this.linie.push_back( this.linie.iat(this.linie.size()-3) );
                    
                       
                   }
                  
                  
                   
                   
            }
        }
        
        
               
       this.pokaz_linie = function( tmp, okno, kolor) // tmp - maciora
       {

	var col =   kolor || "Black";

    	   var width = okno.w;
	   var height = okno.h;

    	   var ind;
    	   var x1,x2,y1,y2;

	   var i;

	   var Twierzcholki = this.Twierzcholki;
	   var Tlinie = this.Tlinie;

	
	var p1 = this.p1;
	var p2 = this.p2;



    	   for(i = 0; i < this.wl ; i += 2 )
    			{
    				ind =  Tlinie[i]*3;
    				p1.punkt( Twierzcholki[ind], Twierzcholki[ind+1], Twierzcholki[ind+2]);



				
					

    				p1.rz();
    				
				
				p1.mulp(tmp);
    		

			//	console.log( p1.getx() +"  " + p1.gety()  );
    				
    				ind =  Tlinie[i+1]*3;
    				p2.punkt( Twierzcholki[ind], Twierzcholki[ind+1], Twierzcholki[ind+2]);
    				p2.rz();
    				p2.mulp(tmp);
			
    				
				
    				p1.na_ekran(width, height);
    		    		p2.na_ekran(width,height);
    				
    				x1 = p1.getx();
    				x2 = p2.getx();
    				
    				y1 = p1.gety();
    				y2 = p2.gety();
    	
				
				

    				if( p1.na_ekranie(width, height) == true && p2.na_ekranie(width,height) == true)
    					okno.drawLine(x1 , y1 ,x2 , y2, 1, col);
	
    				
    			}
    	   
       }
       
       
       this.przepisz = function()
       {
            this.Twierzcholki = new Array( this.wierzcholki.size() +10 );
            this.Trys_w = new Array( this.rys_w.size() +10 );
            this.Trys_n = new Array( this.rys_n.size() +10 );
            this.Trys_t = new Array( this.rys_t.size() +10 );
            this.Tlinie = new Array( this.linie.size() +10 );
            
            this.ww = this.wierzcholki.size();
            this.wrw = this.rys_w.size();
            this.wrn = this.rys_n.size();
            this.wrt = this.rys_t.size();
            this.wl = this.linie.size();
            
	   var i;
            
            for( i = 0 ; i < this.ww ; i ++)
            {
                 this.Twierzcholki[i] = this.wierzcholki.fat(i);
       //          Twierzcholki[i] = 1;
            }
            
            for( i = 0 ; i < this.wrw ; i ++)
            {
                 this.Trys_w[i] = this.rys_w.fat(i);
            }
            
            for( i = 0 ; i < this.wrn ; i ++)
            {
                 this.Trys_n[i] = this.rys_n.fat(i);
            }
            
            for( i = 0 ; i < this.wrt ; i ++)
            {
                 this.Trys_t[i] = this.rys_t.fat(i);
            }
            
            for( i = 0 ; i < this.wl ; i ++)
            {
                this.Tlinie[i] = this.linie.iat(i);
               
         ///        Tlinie[i] = 1;
            }
  
            
            
       }


this.czytaj_obj(nazwa);
this.przepisz();      
       
       
}
