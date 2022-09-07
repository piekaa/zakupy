

//kostka = new dane_obj( "http://localhost/czyde/kostka.obj" );


var okno = new Okno(800,600);



$("body").append( okno.canvas );


var klawiatura = new Array();
var mx = okno.w/2, my = okno.h/2;

pan = new Panel( okno );


window.onkeydown = function (e) {
    var code = e.keyCode ? e.keyCode : e.which;
   

	//console.log( code );

    klawiatura [ code ] = 1;
 
};



window.onkeyup = function (e) {
    var code = e.keyCode ? e.keyCode : e.which;
   

    klawiatura [ code ] = 0;
 
};



setInterval( function() 
{
	pan.apdejt(klawiatura, mx, my);
	pan.paint();

},
	1000/60 );




$("canvas").mousemove( function (e) 
{
 mx = e.pageX; 
 my = e.pageY; }  
);

