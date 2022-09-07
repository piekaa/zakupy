
function vector()
{


	this.sizee = 0;
	this.t = new Array();
		

	
	this.pokaz = function()
	{
		console.log("");
		var s = "";
		var i;
			for( i = 0 ; i < this.sizee ; i ++)
			{
				s+= this.t[i]+" ";
			}
		console.log(s);
		console.log("");
	}
	
	
	this.push_back = function( el)
	{
			this.t[this.sizee++] = el;

	}
	
	this.size = function()
	{
		return this.sizee;
	}
	
	this.iat = function( i)
	{
		return this.t[parseInt(i)];
	}
	
	this.fat = function( i)
	{
		return this.t[parseInt(i)];
	}
	

	
}
