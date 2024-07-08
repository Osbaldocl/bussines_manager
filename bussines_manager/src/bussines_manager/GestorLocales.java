package bussines_manager;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GestorLocales {
	private List<Local> locales;
	
	public GestorLocales() {
		locales = new ArrayList<>();
		cargarLocalesDesdeBaseDeDatos();
	}
	
	public void agregarLocal(Local local) {
		locales.add(local);
		guardarLocalEnBaseDeDatos(local);
	}
	
	public List<Local> obtenerLocales() {
		return locales;
	}
	
	public void guardaLocal(Local local) {
		guardarLocalEnBaseDeDatos(local);
	}
	
	public Local obtenerLocalMasCercano(double latitud, double longitud) {
		Local localMasCercano = null;
		double distanciaMinima = Double.MAX_VALUE;
		
		for(Local local : locales){
			double distancia = calcularDistancia(latitud, longitud, local.getLatitud(), local.getLongitud());
			if(distancia < distanciaMinima) {
				distanciaMinima = distancia;
				localMasCercano = local;
			}
		}
		
		return localMasCercano;
	}
	private double calcularDistancia(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371; // km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                   Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                   Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }

    private void guardarLocalEnBaseDeDatos(Local local) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "INSERT INTO mysql.locales (nombre, direccion, latitud, longitud) VALUES (?, ?, ?, ?)";
            PreparedStatement statement = connection.prepareStatement(query);
            statement.setString(1, local.getNombre());
            statement.setString(2, local.getDireccion());
            statement.setDouble(3, local.getLatitud());
            statement.setDouble(4, local.getLongitud());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void cargarLocalesDesdeBaseDeDatos() {
        try (Connection connection = DatabaseConnection.getConnection()) {
            String query = "SELECT id, nombre, direccion, latitud, longitud FROM locales";
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                String nombre = resultSet.getString("nombre");
                String direccion = resultSet.getString("direccion");
                double latitud = resultSet.getDouble("latitud");
                double longitud = resultSet.getDouble("longitud");
                Local local = new Local(nombre, direccion, latitud, longitud);
                locales.add(local);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}