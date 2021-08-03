package ir.bvar.imenfood.interfaces;

import ir.bvar.imenfood.models.FactorProduct;

/**
 * Created by rezapilehvar on 21/1/2018 AD.
 */

public interface SendFactorRequestDialogListener {
    void requestProvider(FactorProduct factorProduct, int position);

    void requestProduct(FactorProduct factorProduct, int position);
}
