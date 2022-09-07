function Stos()
{
	this.t = new Array();
	this.size = 0;

	this.push = function( e )
	{
		m = new Maciora(4,4,20);
		m.klon(e);
		this.t[this.size++] = m;
	}

	
	this.pop = function()
	{
		return this.t[--this.size];
	}

}
