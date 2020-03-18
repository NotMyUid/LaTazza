package application.utils;

public enum TipoCialda {
	caff�,
	caff�Arabica,
	th�,
	th�Limone,
	cioccolata,
	camomilla;
	
	
	public static TipoCialda fromString(String str) {
	    switch(str) {
	    case "caff�Arabica":
	        return TipoCialda.caff�Arabica;
	    case "caff�":
	        return TipoCialda.caff�;
	    case "camomilla":
	        return TipoCialda.camomilla;
	    case "th�":
	        return TipoCialda.th�;
	    case "th�Limone":
	        return TipoCialda.th�Limone;
	    case "cioccolata":
	        return TipoCialda.cioccolata;
	    default:	
	    	return TipoCialda.th�;
	    }
	}
}