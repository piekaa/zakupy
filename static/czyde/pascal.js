function Pascal(max)
{
	this.size = max;
	this.t = new Array(max + 5);
	var i;
	
	for( i = 0 ; i < max + 5 ; i++ ) 
	{
		this.t[i] = new Array( max +5 );	
	}	



	

	this.oblicz = function()
	{
		var i,j;
		for(j=0;j<this.size;j++)
		{
			this.t[0][j]=1;
		}
		
		for(i=1;i<this.size;i++)
		{
			this.t[i][0]=0;
		}
		
		for(i=1;i<this.size;i++)
		{
			for(j=1;j<this.size;j++)
			{
				this.t[i][j]= this.t[i-1][j-1] + this.t[i][j-1];
			}
		}
	}


	this.niuton = function(n,k)
	{
		return this.t[k][n];
	}

	this.get = function(x,y)
	{
		return this.t[x][y];
	}


	this.pokaz = function()	
	{
		var i;
		var j;
		var size = this.size;
		var t = this.t;
		for(j=0;j<size;j++)
		{
			for(i=0;i<size;i++)
			{
				document.write(t[i][j] + " ");
			}
			document.write("<br/>");
		}
	}

	


}


