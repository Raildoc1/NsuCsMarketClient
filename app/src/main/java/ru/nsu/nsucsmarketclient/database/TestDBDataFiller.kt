package ru.nsu.nsucsmarketclient.database

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

class TestDBDataFiller {
    fun fill(imagesDao: ImagesDao) {
        CoroutineScope(Dispatchers.IO).launch {
            imagesDao.insertAll(
                ImageRef(
                    "4656080287_519977179",
                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpopuP1FA957PfMYTxW09izh4WZg8j5Nr_Yg2Yf68Qh3uuZpI_w0VC1-BFlNj-iI9SUIQBvZl2Bq1G6w-vv0Z7qvJ_Bm2wj5HfXJQyDPg"
                ),
                ImageRef(
                    "4571592871_519977179",
                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpopbuyLgNv1fX3eSR96NmlkZKfqPX4PLTcqWZU7Mxkh6eQ89Wt0Qbj_Es-YG76IoHBcwZqaQ2E_VK8lLrugpK76J-awSRgvSV0-z-DyE7TLl_x"
                ),
                ImageRef(
                    "4571593888_519977179",
                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXH5ApeO4YmlhxYQknCRvCo04DEVlxkKgpou6ryFBRw7OnNcy9D7927q5KOk8j5Nr_Yg2Yf7ZIh0u-Q89rzi1fgqRJrYWnxdYeccQc6YlqB-VfswO_njMe5vZubzGwj5Hfi35zU2w"
                ),
                ImageRef(
                    "3106076656_0",
                    "-9a81dlWLwJ2UUGcVs_nsVtzdOEdtWwKGZZLQHTxDZ7I56KU0Zwwo4NUX4oFJZEHLbXU5A1PIYQNqhpOSV-fRPasw8rsUFJ5KBFZv668FFUxnaPLJz5H74y1xtTcz6etNumIx29U6Zd3j7yQoYih3lG1-UJqY27xJIeLMlhpaD9Aclo"
                ),
                ImageRef(
                    "1989281736_302028390",
                    "IzMF03bi9WpSBq-S-ekoE33L-iLqGFHVaU25ZzQNQcXdB2ozio1RrlIWFK3UfvMYB8UsvjiMXojflsZalyxSh31CIyHz2GZ-KuFpPsrTzBG0pPSEEEvycTKKfXSJTA88RLBYZm_d-Df2s7udQ2ydQLl5S18FL_BSp2wca8vca0E5hZlLpWL-lEtxEQQlZ8lSeR-30ylKNehznyD_8PAlXw"
                )
            )
        }
    }
}