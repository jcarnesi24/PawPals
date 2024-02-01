package pawpals_db.Pets;

import javax.websocket.EncodeException;
import javax.websocket.Encoder;
import javax.websocket.EndpointConfig;
import com.google.gson.Gson;

public class PetEncoder implements Encoder.Text<Pet> {
    private static final Gson GSON = new Gson();

    @Override
    public String encode(Pet pet) throws EncodeException {
        return GSON.toJson(pet);
    }

    @Override
    public void init(EndpointConfig config) {
        // Initialization logic, if needed
    }

    @Override
    public void destroy() {
        // Cleanup logic, if needed
    }
}

