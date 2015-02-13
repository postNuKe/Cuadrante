package es.progmac.cuadrante.lib;

public class Extra {
    /**
     * Inicio de variables extras entre activitys
     */
    public static final String EXTRA = "es.progmac.cuadrante";
    /**
     * Variable mode, inicialmente para LoginActivity, si se muestra en estado
     * de login o de edit preferences
     */
    public static final String MODE = EXTRA.concat(".mode");
    /**
     * Se usa para AlarmDeleteFileReceiver
     */
    public static final String FILENAME = EXTRA.concat(".filename");
    /**
     * Se usa para el MainActivity hacia ServicioActivity
     */
    public static final String DATE = EXTRA.concat(".date");
    /**
     * Se usa para el SearchResult hacia Main
     */
    public static final String DAY = EXTRA.concat(".day");
    /**
     * Se usa para el SearchResult hacia Main
     */
    public static final String MONTH = EXTRA.concat(".month");
    /**
     * Se usa para el SearchResult hacia Main
     */
    public static final String YEAR = EXTRA.concat(".year");
    /**
     * Se usa para el TipoServicioListActivity hacia TipoServicioActivity
     */
    public static final String TYPE_ID = EXTRA.concat(".type_id");
    /**
     * Se usa para el generar el backup desde Cuadrante.autoBackupDB
     */
    public static final String AUTO_BACKUP = EXTRA.concat(".auto_backup");
    /**
     * Para guardar la fecha de inicio de la comisión
     */
    public static final String COMISION_START_DATE = EXTRA.concat(".comision_start_date");
    /**
     * Para guardar la fecha fin de la comisión
     */
    public static final String COMISION_END_DATE = EXTRA.concat(".comision_end_date");
    /**
     * Comission_id
     */
    public static final String COMISION_ID = EXTRA.concat(".comision_id");


    //HOTEL
    public static final String TMP_HOTEL_ID = EXTRA.concat(".tmp_hotel_id");
    public static final String HOTEL_ADD = EXTRA.concat(".hotel_add");
    public static final String NAME = EXTRA.concat(".name");
    public static final String COMMENTS = EXTRA.concat(".comments");
    public static final String START_DATE = EXTRA.concat(".start_date");
    public static final String END_DATE = EXTRA.concat(".end_date");
    public static final String DAILY_EXPENSES = EXTRA.concat(".daily_expenses");
    public static final String MANUTENCION_EXPENSES = EXTRA.concat(".manutencion_expenses");
    public static final String LAUNDRY = EXTRA.concat(".laundry");

    public static final String TURN_ID = EXTRA.concat(".turn_id");
    public static final String SEARCH_STRING = EXTRA.concat(".search_string");
    public static final String SEARCH_DATE_START = EXTRA.concat(".search_date_start");
    public static final String SEARCH_DATE_END = EXTRA.concat(".search_date_end");
    public static final String TYPES_SERVICES = EXTRA.concat(".types_services");


}
