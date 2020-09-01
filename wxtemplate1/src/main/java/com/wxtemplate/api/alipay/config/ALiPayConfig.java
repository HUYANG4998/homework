package com.wxtemplate.api.alipay.config;

/**
 * 支付宝支付所需的必要参数 理想情况下只需配置此处
 */
public class ALiPayConfig {
	// 合作身份者ID，以2088开头由16位纯数字组成的字符串
	public static String partner = "2088431310717610";
//	public static String partner = "2088102177574060";

	// appid
//	public static String appid = "2018071960697512";
	public static String appid = "2016101800718262";

	// 商户支付宝账号
//	public static String seller_email = "xuzhouzaichufa@126.com";
	public static String seller_email = "pnclml4657@sandbox.com";

	// 商户真实姓名
//	public static String account_name = "江苏省徐州市再出发网络科技有限公司";
	public static String account_name = "苏州世歆信息技术有限公司"; 

	// URL
	public static String url = "https://openapi.alipaydev.com/gateway.do";
	public static String timeoutExpress = "1000";

	// 商户的私钥RSA
//	public static String private_key = "MIIEvwIBADANBgkqhkiG9w0BAQEFAASCBKkwggSlAgEAAoIBAQCJBZ5f513t1fsJbWGoxsyPDMuye8KkXyQ1WcCBCyAT7Kin3SrULK/01AD/M12BxHXgh364Me1m7iON8YTepJeNAixMeIxrjAelXhudjccYnvLUC7izr6/EOin3Dh30OS2Mrp4JUdF8OOd9/93fqwrxuvTV0nkDkNc0GpJyBU9MU8rypVxuaMBV0lCFiBhfMQ/XeZ5TLsZ0jztyLzcrWse198VsEQbJ/skLNTllen76dQH/kw3AQ9j2pFgeavUJOEZFdpzhWVykpsQdKwR0a4M65BbmxzbKk3KmU9779613npS7Nthp9Oa0fWYBGeel8WoBMMhFX264i6vadAEAGpPVAgMBAAECggEADjVy8ub/Koah/ZxGIEZdOAhPpzaGPJ1RF+cyuy74KJjF9IJOyqnHpU/GurAM5kQFJT9nuJoU0DOppDKDYMMYpBqxlUx64zwmHtPfcTsehkMVUz/T624wf9y4NVJVcNntC0WgRb8iRgtwlfROgb8u51pHwVSOD/yZFGIdBY3fZz6yP/RnHts8Zy24vl4ZOnMxs9w6lmx/rQzM7W7e/6dYQN56cMBuYpe4DMT1lpEasD1py6j8yQKtrYyskl8djMtqKydKUfvzAuIiTB8H1kWtU5sOTJ1DxhRzoqfqLENCJt81W6bfE3LapVzcUTnBLWmfT07ErcaQqOQkOVIaqyNBgQKBgQDJD83uSmYw8xYCTK/79ShyXsDJibXpcFsy0aobGSDXXuhwyrK+rQZ74Sp8lfKfnOXPUS6ku9icQLtHoy0uGj3TA3bQcF6WxqnN/cMT/bXdEuxEIefsurEQvb+qHei5p+j5qY2A9b4SHmD7eoowv7xoFtGwVnyDeK5EfFCITt+DPQKBgQCudkEW7Tm8WNyNp1V9R1yv48fOgmMWNx00/hkO3gD8iW8JUieeVkxuJOGDkkEoZKMpTH+D6cTwdaIqlZ8eBzNx9KWjaMHuJE+jd4CAT6CRxDSgANuoj6ZZM+8vIo/905Nkt7Lh1q8kb9uP0J3JnVMdmuLvrpNrpYqj4n7uxrR8eQKBgQDEjUmwUFT0Nz7lwpgrhC8lF1H580C9UxQkauiTEw6S3Hn4gX8ZfcYf5i+FmYSU2mmiMOebLPOFVJaAYplxPz/+/5zwCVm+pbkr4Y4KBfT6iFwmAstaFeuwxP4QTKrMi8PqBQGK9zD3P3FphhJ/s4B4dQ5KB1IzqA8cbja/+fLwtQKBgQCfSHJW91HeR5decWeZEN1r7WBQiDTlZH5zEodYTpLB/sx0yyBG7O2tJlkLIi4BLhjVrPVDP9zB2fSsQpza8qIiqcXM8ukUfuyDB6k3/PtR+rw9VWs3c6fiC4uWEk065r/MlfdpP/P6JXJl1IcrO0tWXUJSqKYKA0MYK8POR3cjqQKBgQC5wJrarFXio5qmYNqF/HePl1kgtsTeQ7j8c1Qz73OEKeOmwQbslqxUotV2QUmseM0+bStWCkC3m6ue9LkMu8ik4cEIIAoKO4yDJKsp/PLxxUoxLDyq5uEIh2Z9BRZaoR3V9bV4O2YleqWF/IBywvcU8eTrxcw1jrzL+sC62GAItw==";
	public static String private_key = "MIIEvgIBADANBgkqhkiG9w0BAQEFAASCBKgwggSkAgEAAoIBAQCATpAuadXIjS5iEBRlm6Pm4DE5j1Sd9ymZbVSWLDOz9JR5wyoMXP8JB837ty461gkbJGGGM1DSrRgydiTCzrDZNMvAUZtWta6Q6aIhmjODSVdpGXv/1aB70VeSmwljNgt51qKEuc1wtwWHwKWVH379GYPY1LPKPRzQJKI4hOB9r3Gno6pRpifE+KXzB4eJn1c3fj+FJdTGkHBNi6+d30ZFb68RV/MIqy6CLvIV5t3Z91qM3woXd1NCujTOMFmlG2cCDTnhzAHzR1ALc/lsgsn0EQ857Utvil40ewIeJ7O8EM1v1p7LkrcNVO43dRnflATjxsWRS0h9XW426XEMQyIFAgMBAAECggEAZemBHkyUMRrqp5tsCBTqx9x1XOna5SnH0gUENc/gNWTkdh6tjGplbG/VjcU/JB6T1wo7qFOkM+KnIJW0t0NjNvN3j0eCognnIVVhoV0Q44visSk708aEFTT/YQRiHXp0/Q6dwdyvMIM/EkfTfbIptLGKhzTTpaSK+UzPySLKCSmso64gm8HuDrSViytjvvuwyC30h2tFD6XkHoDT9AcimgTCRXMwOboy/16ZqlngJtucSc2Av3SszZFrAs8tbh0EfqnlCTRrCYkcc0AyQIPYS+j4cV3OVtbEHHNSAUJwPgYMHRgIWFwmDgpKtvtOPP2LsPqHDRIEeeX8eTuD9gne9QKBgQC4HAwaMbws3r/DLKO374N2jgKeK4+hZGNimYz1ewbi/LfcCXgdmgFnGBQoHdjfHAsuuoFbCmLJ+bh4w9HSvC6/akr3unHfX859JjMi/3eXLVyZoUEZ0u+YK74amhr/RcM4qW6wwQ3xfuBP7ky92vmIyE0nmLC7yOPpxMJlqdMeowKBgQCyaF8R5y3BFOZHcK51tNDTuUfK2Z1g4lODMG/srEPYYL66RKq3o+RmwXkBY4R1lWpI0gcTyf+R8cH2Ky4tnVReof2yhE/9APP2amHD5ZXWzubWGmUm0FcRxfS5lHulEmKZd0qY9HqtnYWvPcNJQ8FEyxxp0asmUfUgV2tDqDQPNwKBgGOS38THA2QtMP+pQ/62Lp7PHYFYQ/1g4xOvwnn+CPJ5BiOeh3VTFtEgR76Orf21GCM0wcNwcY+VXP0Lw467MnlaTqN/FhleorozD9Ms4gxEf2GjwibqW6Use0u97z4DCBu8H16/ewASw9TXKHBCX1VoJ7pzrZpSg1mqjg0X9VQLAoGBAId+hv2E0pX6dm25YXYZriOuapzfqwvCt+RBrC9WIWy7BP9YWUOBIFz+ARWR7C6VHJRTlnZsCkJgFowL/VU9UK9SXefvXwRqbd4uqzNpMORkXKIsh7fzsLzZ1+P9pe4XSXT+SVRjEKy8nqQZxy/kWmsRyvF/AHno8r7uRZOrsmX/AoGBALQ8YqJmUxyW+zmSgjGW1CbgO7q/MUUPSsbrpb3E4n4J3tjrTRTYDWvgPIG4EI/k+ekxEmaybtL/pM8C5/aeMsK9uiwS4oVV5aQmn/Lmh18IVIM+Y37ljDuyRKwR6UZ7MgkmtzDYVuJmosLWz1W7ZnmSanBEpT10pfsjrt66iCpD";
	// 支付宝的公钥 RSA
//	public static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEArqoTa6tSlY5H1O6HTbqruMeNpS7RwlrJy5+1+zXG6uTCe6VWOLO2xPeXpSbJBeY5FBJ3LlqTgIixr3VJthA0IF+1ChaY6ZYDKkBA3cbzQWemPGoYOifTHvzigzThVPCp2WnvLtCQIDVW40kRy/HNkNQGmZldtF1/4owwATJm4ChvH6e72izyQkK7NsYYxsaxE6P6biLiaPH2MR4kv4emGxIxfRfUp0CELolxNStgAONvO4m/HxyCO34DtnIJuz3DavJAdCldZLXWY2zGz/RnGsYsmdDDN8MKGL5vIKSVEqbLrJx7sJW0BqHEnL15DBQcNArHgSblelp/89GrA0Z+QQIDAQAB"; // 签名方式
	public static String ali_public_key = "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtcGuMnkMfbxmHmIhYi9TBWHuhYmzpHMx5LmMvzpuE4f0oVzBlGqrbLdpHFweFv4V0CHq5VI3aXy7ljoi6gyIQISkjQBDD+W2Fn7eILpNuuzMgejxZVM56D3sa5o9XFo8FBhstRe2BE7KZBlshZN1RN3FOmth8NKNM0CoVL3LlCivDKnUJN1zExKnwS9qQ/S37T7G0gWYUvH0/Cqyzg61LnCY1iQBQ2UYrQ0hpb0EwP/nkFE3misyjV8/ygbvPq9Qy3fRCb0+70EjrJrQDFb6ddDqK021CymM4NW3z2zw2CNPcvLZdtj06YcPPjN7Ua1YmXUQIbWvO+h2CDIQWL2y7QIDAQAB"; // 签名方式
																																																																																																														// (支付回调签名方式)
	public static String pay_sign_type = "RSA2";

	/**
	 * 支付宝服务器主动通知商户服务器里指定的页面http/https路径。建议商户使用https
	 * 这里需要测试的话，需要用外网测试。https://www.ngrok.cc/ 这里有免费和付费的，实际上，免费用一下就可以了。
	 */
	public static String notify_url = "	http://47.100.110.119:9621/run/Alipay/notify_url";
	// 商品的标题/交易标题/订单标题/订单关键字等。
	public static String subject = "商品购买";

	// ↑↑↑↑↑↑↑↑↑↑请在这里配置您的基本信息↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

	// 接口名称 固定为：alipay.trade.app.pay
	public static String method = "alipay.trade.app.pay";

	// 调用的接口版本，固定为：1.0
	public static String version = "1.0";


}
