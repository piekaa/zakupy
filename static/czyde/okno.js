function Okno(w,h)
{
	this.w = w;
	this.h = h;
	this.canvas = document.createElement("CANVAS");

	this.canvas.width = w;
	this.canvas.height = h;
	this.ctx = this.canvas.getContext("2d");


	this.drawLine = function(x1,y1,x2,y2, lineWidth, color )
			{


			
				this.ctx.lineWidth = lineWidth || 1;
				this.ctx.strokeStyle =  color || "#000000";
				this.ctx.beginPath();
				this.ctx.moveTo(x1,y1);
				this.ctx.lineTo(x2,y2);
				this.ctx.stroke();
			}

	this.clear = function()
	{
		this.ctx.clearRect(0, 0, this.w , this.h);
	}


}

/*
var okno = new Okno(200,200);



$("body").append( okno.canvas );


okno.drawLine(50,50,10,60, 1, "#000000" );
okno.drawLine(10,10,50,50,1, "#000000");
okno.clear();
okno.drawLine(60,70,20,0);
*/
