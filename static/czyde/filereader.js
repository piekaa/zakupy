
function readFile(url)
{
this.plik="";
this.p = 0;

var d;

$.ajax(
					{
						type :"GET",
						url  : url,
						async : false,
						dataType: "text",
						success : function(dane)
						{
							d = dane;
						},						
						error : function(error)
						{
							alert("Blad! "+error);
						}
					}
				     );

this.plik = d;


		this.hasNextLine = function()
		{
				if( this.p < this.plik.length )
					return true;
				return false;
		}


		this.nextLine = function()
		{
			var s="";
			for(  1 ; this.p < this.plik.length ; this.p++ )
			{
				if( this.plik.charAt(this.p) != '\n' )
				{
					s += this.plik.charAt(this.p);
				}
				else
				{
					this.p++;
					return s;
				}
			}
			return s;
		}


	this.file = function()
	{
		return this.plik;
	}



	




}

