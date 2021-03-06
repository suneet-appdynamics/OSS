/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * __________________
 *
 *  Copyright 2008 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated
 * and its suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/
package flex.messaging.io.amfx.testtypes;

public class Bleu_dAuvergne extends EweCheese
{
    public Bleu_dAuvergne()
    {
        super();
    }

    public int getMaturation() // Weeks
    {
        return 8;
    }

    public String getName()
    {
        return "Bleu d'Auvergne";
    }

    public String getRegion()
    {
        return "Auvergne";
    }

    public Cheese getParing()
    {
        return paring;
    }

    public void setParing(Cheese c)
    {
        paring = c;
    }
}

