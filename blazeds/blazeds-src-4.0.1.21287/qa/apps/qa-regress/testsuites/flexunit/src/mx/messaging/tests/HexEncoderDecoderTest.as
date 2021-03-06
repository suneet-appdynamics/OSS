/*************************************************************************
 *
 * ADOBE CONFIDENTIAL
 * ___________________
 *
 *  Copyright 2008 Adobe Systems Incorporated
 *  All Rights Reserved.
 *
 * NOTICE:  All information contained herein is, and remains
 * the property of Adobe Systems Incorporated and its suppliers,
 * if any.  The intellectual and technical concepts contained
 * herein are proprietary to Adobe Systems Incorporated and its
 * suppliers and may be covered by U.S. and Foreign Patents,
 * patents in process, and are protected by trade secret or copyright law.
 * Dissemination of this information or reproduction of this material
 * is strictly forbidden unless prior written permission is obtained
 * from Adobe Systems Incorporated.
 **************************************************************************/

package mx.messaging.tests
{

import flexunit.framework.*;

import flash.utils.ByteArray;
import mx.controls.Alert;
import mx.utils.*;
 /**
  * Test encoding and decoding of base16 or hexadecimal encoded byte arrays.
  */   
public class HexEncoderDecoderTest extends TestCase
{
    ////////////////////////////////////////////////////////////////////////////
    //
    // Constructor
    //
    ////////////////////////////////////////////////////////////////////////////   
    public function HexEncoderDecoderTest(name : String)
    {
        super(name);
    }
    ////////////////////////////////////////////////////////////////////////////
    //
    // TestSuite
    //
    ////////////////////////////////////////////////////////////////////////////  
    public static function suite() : TestSuite
    {
        var suite : TestSuite = new TestSuite();
        suite.addTest(new HexEncoderDecoderTest("testEncodeDecode"));
        return suite;
    } 
    ////////////////////////////////////////////////////////////////////////////
    //
    // Tests
    //
    ////////////////////////////////////////////////////////////////////////////  
    /**
     * Test encoding and decoding of base16 or hexadecimal encoded byte arrays using the 
     * HexEncoder and HexDecoder classes
     */     
    public function testEncodeDecode() :void
    {
        var randomLimit:int = 500;

        var success:Boolean = true;

        for (var myCount:int = 0; myCount < 100; myCount++)
        {

            var randomLength:Number = Math.random() * randomLimit;
            var rawArray:ByteArray = new ByteArray();
            for (var i:int = 0; i < randomLength; ++i)
            {
                if ((i % 1024) < 256)
                {
                    rawArray.writeByte(i % 1024);
                }
                else
                {
                    rawArray.writeByte(new int((Math.random()) * 255) - 128);
                }
            }
        
            rawArray.position = 0;
            var encoder : HexEncoder = new HexEncoder();
            encoder.encode(rawArray);

            var encoded:String = encoder.drain();

            var decoder : HexDecoder = new HexDecoder();
            decoder.decode(encoded);
            var decoded:ByteArray = decoder.flush();
                
            rawArray.position = 0;
            decoded.position = 0;        
            if (decoded.bytesAvailable != rawArray.bytesAvailable)
            {
                success = false;
            }
            else
            {
                while (decoded.bytesAvailable)
                {
                    if (decoded.readByte() != rawArray.readByte())
                    {
                        success = false;
                        break;
                    }
                }
            }
        
            if (!success)
            {
                break;
            }        
        }
        
        assertEquals(true, success);
    }
}
}